package kr.hhplus.be.server.infrastructure.dao.user

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import kr.hhplus.be.server.domain.model.user.UserWalletHistory
import kr.hhplus.be.server.infrastructure.dao.BaseEntity

@Entity
@Table(name = "user_wallet_history")
class UserWalletHistoryEntity(
    id: Long = 0,

    @Column(nullable = false, unique = true)
    val userWalletId: Long,

    @Column(nullable = false)
    var amount: Long,
) : BaseEntity(id) {

    fun toModel(): UserWalletHistory {
        return UserWalletHistory(
            id = id,
            userWalletId = userWalletId,
            amount = amount
        )
    }

    companion object {
        fun from(userWalletHistory: UserWalletHistory): UserWalletHistoryEntity {
            return UserWalletHistoryEntity(
                id = userWalletHistory.id,
                userWalletId = userWalletHistory.userWalletId,
                amount = userWalletHistory.amount
            )
        }
    }
}