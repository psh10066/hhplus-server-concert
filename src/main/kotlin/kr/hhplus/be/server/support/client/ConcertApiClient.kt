package kr.hhplus.be.server.support.client

import kr.hhplus.be.server.domain.model.concert.Concert
import kr.hhplus.be.server.domain.service.ConcertService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Component
class ConcertApiClient(
    private val concertService: ConcertService
) {
    private val log = LoggerFactory.getLogger(javaClass)

    // 외부 서비스 호출 가정이므로, 트랜잭션 분리
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun reserveSeat(concertSeatId: Long) {
        log.info("[외부 서비스 호출 가정] - 좌석 선점")
        concertService.reserveSeat(concertSeatId)
    }

    // 외부 서비스 호출 가정이므로, 트랜잭션 분리
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun getConcertBySeatId(concertSeatId: Long): Concert {
        log.info("[외부 서비스 호출 가정] - 콘서트 조회")
        return concertService.getConcertBySeatId(concertSeatId)
    }

    // 외부 서비스 호출 가정이므로, 트랜잭션 분리
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun findConcerts(ids: List<Long>): List<Concert> {
        log.info("[외부 서비스 호출 가정] - 콘서트 목록 조회")
        return concertService.findConcerts(ids)
    }

    // 외부 서비스 호출 가정이므로, 트랜잭션 분리
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun completePaymentSeat(concertSeatId: Long) {
        log.info("[외부 서비스 호출 가정] - 좌석 결제 완료 처리")
        concertService.completePaymentSeat(concertSeatId)
    }

    // 외부 서비스 호출 가정이므로, 트랜잭션 분리
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun rollbackPaymentSeat(concertSeatId: Long) {
        log.info("[외부 서비스 호출 가정] - 좌석 결제 롤백 처리")
        concertService.rollbackPaymentSeat(concertSeatId)
    }
}