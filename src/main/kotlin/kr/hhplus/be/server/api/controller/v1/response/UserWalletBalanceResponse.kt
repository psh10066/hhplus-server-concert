package kr.hhplus.be.server.api.controller.v1.response

data class UserWalletBalanceResponse(
    val userId: Long,
    val balance: Long
)