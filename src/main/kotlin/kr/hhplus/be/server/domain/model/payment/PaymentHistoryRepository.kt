package kr.hhplus.be.server.domain.model.payment

interface PaymentHistoryRepository {

    fun save(paymentHistory: PaymentHistory): PaymentHistory
}