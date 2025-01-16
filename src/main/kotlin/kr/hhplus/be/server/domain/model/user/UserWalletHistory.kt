package kr.hhplus.be.server.domain.model.user

class UserWalletHistory(
    val id: Long = 0,
    val userWalletId: Long,
    var amount: Long,
)