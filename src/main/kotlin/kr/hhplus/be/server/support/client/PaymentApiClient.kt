package kr.hhplus.be.server.support.client

import kr.hhplus.be.server.domain.model.payment.PaymentHistory
import kr.hhplus.be.server.domain.service.PaymentService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Component
class PaymentApiClient(
    private val paymentService: PaymentService
) {
    private val log = LoggerFactory.getLogger(javaClass)

    // 외부 서비스 호출 가정이므로, 트랜잭션 분리
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun pay(reservationId: Long, userId: Long, amount: Long): PaymentHistory {
        log.info("[외부 서비스 호출 가정] - 결제 요청")
        return paymentService.pay(reservationId, userId, amount)
    }

    // 외부 서비스 호출 가정이므로, 트랜잭션 분리
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun rollbackPay(paymentHistoryId: Long) {
        log.info("[외부 서비스 호출 가정] - 결제 롤백")
        paymentService.rollbackPay(paymentHistoryId)
    }
}
