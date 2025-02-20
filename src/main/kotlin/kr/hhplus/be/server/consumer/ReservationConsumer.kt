package kr.hhplus.be.server.consumer

import kr.hhplus.be.server.domain.event.listener.CONCERT_RESERVATION_TOPIC
import kr.hhplus.be.server.domain.model.reservation.Reservation
import kr.hhplus.be.server.domain.model.reservation.ReservationOutboxRepository
import kr.hhplus.be.server.domain.model.reservation.ReservationOutboxStatus
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class ReservationConsumer(
    private val reservationOutboxRepository: ReservationOutboxRepository
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @KafkaListener(topics = [CONCERT_RESERVATION_TOPIC], groupId = "reservation-group")
    fun consumeConcertReservation(reservation: Reservation) {
        reservationOutboxRepository.findByOperationTypeAndOperationId(
            operationType = "concertReservation",
            operationId = reservation.id.toString()
        )?.let { outbox ->
            outbox.status = ReservationOutboxStatus.PUBLISHED
            reservationOutboxRepository.save(outbox)
        } ?: let {
            log.error("[예약] Outbox에 존재하지 않는 예약 reservationId=[${reservation.id}]")
        }
    }
}