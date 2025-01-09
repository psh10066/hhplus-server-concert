package kr.hhplus.be.server.domain.model.payment

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import kr.hhplus.be.server.infrastructure.dao.BaseEntity

@Entity
@Table(name = "payment_history")
class PaymentHistory(
    @Column(nullable = false)
    val reservationId: Long,

    @Column(nullable = false)
    val userId: Long,

    @Column(nullable = false)
    var amount: Long
) : BaseEntity()