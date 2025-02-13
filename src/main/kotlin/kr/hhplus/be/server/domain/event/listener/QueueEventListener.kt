package kr.hhplus.be.server.domain.event.listener

import kr.hhplus.be.server.domain.event.ConcertReservationFinishedEvent
import kr.hhplus.be.server.domain.event.ReservationConcertPaymentSucceedEvent
import kr.hhplus.be.server.domain.service.QueueService
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class QueueEventListener(
    private val queueService: QueueService
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Async
    @TransactionalEventListener(value = [ConcertReservationFinishedEvent::class], phase = TransactionPhase.AFTER_COMMIT)
    fun readyPayment(event: ConcertReservationFinishedEvent) {
        try {
            queueService.readyPayment(event.user.uuid)
        } catch (e: Exception) {
            log.error("[대기열] 결제 준비 실패", e)
        }
    }

    @Async
    @EventListener(value = [ReservationConcertPaymentSucceedEvent::class])
    fun expire(event: ReservationConcertPaymentSucceedEvent) {
        try {
            queueService.expire(event.user.uuid)
        } catch (e: Exception) {
            log.error("[대기열] 만료 처리 실패", e)
        }
    }
}