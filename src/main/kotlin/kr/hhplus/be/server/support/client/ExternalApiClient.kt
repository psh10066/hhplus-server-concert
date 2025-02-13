package kr.hhplus.be.server.support.client

import kr.hhplus.be.server.domain.model.reservation.Reservation
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class ExternalApiClient {
    private val log = LoggerFactory.getLogger(javaClass)

    fun sendReservationInfoToDataPlatform(reservation: Reservation) {
        log.info("[외부 서비스 호출 가정] - 데이터 플랫폼 예약 정보 전송")
        try {
            Thread.sleep(3000) // 외부 서비스 요청 3초 소요
            log.info("[데이터 플랫폼] API 요청 성공")
        } catch (e: Exception) {
            log.error("[데이터 플랫폼] API 요청 실패")
        }
    }
}
