package kr.hhplus.be.server.infrastructure.dao.payment

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import kr.hhplus.be.server.domain.model.payment.PaymentHistory
import kr.hhplus.be.server.infrastructure.dao.BaseEntity

@Entity
@Table(name = "payment_history")
class PaymentHistoryEntity(
    id: Long = 0,

    @Column(nullable = false)
    val reservationId: Long,

    @Column(nullable = false)
    val userId: Long,

    @Column(nullable = false)
    var amount: Long
) : BaseEntity(id) {

    fun toModel(): PaymentHistory {
        return PaymentHistory(
            id = id,
            reservationId = reservationId,
            userId = userId,
            amount = amount,
        )
    }

    companion object {
        fun from(paymentHistory: PaymentHistory): PaymentHistoryEntity {
            return PaymentHistoryEntity(
                id = paymentHistory.id,
                reservationId = paymentHistory.reservationId,
                userId = paymentHistory.userId,
                amount = paymentHistory.amount,
            )
        }
    }
}