package kr.hhplus.be.server.domain.model.user

interface UserWalletHistoryRepository {

    fun save(userWalletHistory: UserWalletHistory): UserWalletHistory
}