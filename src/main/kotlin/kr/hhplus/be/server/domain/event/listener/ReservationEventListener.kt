package kr.hhplus.be.server.domain.event.listener

import kr.hhplus.be.server.domain.event.ConcertReservationSucceedEvent
import kr.hhplus.be.server.support.client.ExternalApiClient
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class ReservationEventListener(
    private val externalApiClient: ExternalApiClient
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Async
    @TransactionalEventListener(value = [ConcertReservationSucceedEvent::class], phase = TransactionPhase.AFTER_COMMIT)
    fun sendReservationInfoToDataPlatform(event: ConcertReservationSucceedEvent) {
        try {
            externalApiClient.sendReservationInfoToDataPlatform(event.reservation)
        } catch (e: Exception) {
            log.error("[예약] 데이터 플랫폼 전송 실패", e)
        }
    }
}