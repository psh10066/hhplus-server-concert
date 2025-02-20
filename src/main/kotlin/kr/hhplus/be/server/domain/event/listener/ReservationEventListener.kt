package kr.hhplus.be.server.domain.event.listener

import com.fasterxml.jackson.databind.ObjectMapper
import kr.hhplus.be.server.domain.event.ConcertReservationSucceedEvent
import kr.hhplus.be.server.domain.model.reservation.Reservation
import kr.hhplus.be.server.domain.model.reservation.ReservationOutbox
import kr.hhplus.be.server.domain.model.reservation.ReservationOutboxRepository
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

const val CONCERT_RESERVATION_TOPIC = "concert-reservation"

@Component
class ReservationEventListener(
    private val reservationOutboxRepository: ReservationOutboxRepository,
    private val kafkaTemplate: KafkaTemplate<String, Reservation>,
    private val objectMapper: ObjectMapper
) {

    @TransactionalEventListener(value = [ConcertReservationSucceedEvent::class], phase = TransactionPhase.BEFORE_COMMIT)
    fun putConcertReservationOutbox(event: ConcertReservationSucceedEvent) {
        val reservationOutbox = ReservationOutbox.create(
            operationType = "concertReservation",
            operationId = event.reservation.id.toString(),
            topic = CONCERT_RESERVATION_TOPIC,
            message = objectMapper.writeValueAsString(event.reservation)
        )
        reservationOutboxRepository.save(reservationOutbox)
    }

    @TransactionalEventListener(value = [ConcertReservationSucceedEvent::class], phase = TransactionPhase.AFTER_COMMIT)
    fun sendKafka(event: ConcertReservationSucceedEvent) {
        kafkaTemplate.send(CONCERT_RESERVATION_TOPIC, event.reservation)
    }
}