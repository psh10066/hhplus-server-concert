package kr.hhplus.be.server.domain.model.user

interface UserWalletRepository {

    fun getByUserId(userId: Long): UserWallet
}