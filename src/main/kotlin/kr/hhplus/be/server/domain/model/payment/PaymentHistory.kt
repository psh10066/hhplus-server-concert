package kr.hhplus.be.server.domain.model.payment

class PaymentHistory(
    val id: Long = 0,
    val reservationId: Long,
    val userId: Long,
    val amount: Long
)