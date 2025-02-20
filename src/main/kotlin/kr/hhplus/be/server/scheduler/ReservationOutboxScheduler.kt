package kr.hhplus.be.server.scheduler

import com.fasterxml.jackson.databind.ObjectMapper
import kr.hhplus.be.server.domain.model.reservation.Reservation
import kr.hhplus.be.server.domain.model.reservation.ReservationOutboxRepository
import kr.hhplus.be.server.domain.model.reservation.ReservationOutboxStatus
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.Clock
import java.time.LocalDateTime
import kotlin.time.Duration.Companion.minutes

@Component
class ReservationOutboxScheduler(
    private val clock: Clock,
    private val objectMapper: ObjectMapper,
    private val kafkaTemplate: KafkaTemplate<String, Reservation>,
    private val reservationOutboxRepository: ReservationOutboxRepository,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Scheduled(fixedRate = 5000)
    fun kafkaRePublish() {
        val interval = 5.minutes
        val date = LocalDateTime.now(clock).minusSeconds(interval.inWholeSeconds)

        val outboxes = reservationOutboxRepository.findAllByStatusAndCreatedAtBefore(ReservationOutboxStatus.CREATED, date)

        outboxes.forEach { outbox ->
            try {
                val reservation = objectMapper.readValue(outbox.message, Reservation::class.java)
                if (outbox.key != null) {
                    kafkaTemplate.send(outbox.topic, outbox.key, reservation)
                } else {
                    kafkaTemplate.send(outbox.topic, reservation)
                }
            } catch (e: Exception) {
                log.error("[예약] 카프카 재발행 실패", e)
            }
        }
    }
}