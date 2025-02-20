package kr.hhplus.be.server.consumer

import kr.hhplus.be.server.domain.event.listener.CONCERT_RESERVATION_TOPIC
import kr.hhplus.be.server.domain.model.reservation.Reservation
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class ExternalConsumer {
    private val log = LoggerFactory.getLogger(javaClass)

    @KafkaListener(topics = [CONCERT_RESERVATION_TOPIC], groupId = "reservation-group")
    fun consumeDataPlatform(reservation: Reservation) {
        try {
            Thread.sleep(3000) // 외부 서비스 요청 3초 소요
            log.info("[데이터 플랫폼] 예약 정보 수신 성공")
        } catch (e: Exception) {
            log.error("[데이터 플랫폼] 예약 정보 수신 실패")
        }
    }
}