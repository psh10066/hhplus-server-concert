package kr.hhplus.be.server.domain.event.listener

import kr.hhplus.be.server.domain.event.ConcertReservationFinishedEvent
import kr.hhplus.be.server.domain.service.ConcertService
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class ConcertEventListener(
    private val concertService: ConcertService
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(value = [ConcertReservationFinishedEvent::class], phase = TransactionPhase.AFTER_ROLLBACK)
    fun cancelSeatReservation(event: ConcertReservationFinishedEvent) {
        try {
            concertService.cancelSeat(event.concertSeatId)
        } catch (e: Exception) {
            log.error("[콘서트] 좌석 예약 취소 실패", e)
        }
    }
}