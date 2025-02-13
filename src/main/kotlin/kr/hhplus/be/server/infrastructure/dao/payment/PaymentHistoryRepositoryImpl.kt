package kr.hhplus.be.server.infrastructure.dao.payment

import kr.hhplus.be.server.domain.model.payment.PaymentHistory
import kr.hhplus.be.server.domain.model.payment.PaymentHistoryRepository
import org.springframework.stereotype.Component

@Component
class PaymentHistoryRepositoryImpl(
    private val paymentHistoryJpaRepository: PaymentHistoryJpaRepository
) : PaymentHistoryRepository {

    override fun save(paymentHistory: PaymentHistory): PaymentHistory {
        return paymentHistoryJpaRepository.save(PaymentHistoryEntity.from(paymentHistory)).toModel()
    }

    override fun deleteById(id: Long) {
        paymentHistoryJpaRepository.deleteById(id)
    }
}