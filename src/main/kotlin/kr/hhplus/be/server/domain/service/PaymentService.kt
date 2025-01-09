package kr.hhplus.be.server.domain.service

import kr.hhplus.be.server.domain.model.payment.PaymentHistory
import kr.hhplus.be.server.domain.model.payment.PaymentHistoryRepository
import org.springframework.stereotype.Service

@Service
class PaymentService(
    private val paymentHistoryRepository: PaymentHistoryRepository
) {
    fun pay(reservationId: Long, userId: Long, amount: Long): PaymentHistory {
        return paymentHistoryRepository.save(PaymentHistory(
            reservationId = reservationId,
            userId = userId,
            amount = amount
        ))
    }
}