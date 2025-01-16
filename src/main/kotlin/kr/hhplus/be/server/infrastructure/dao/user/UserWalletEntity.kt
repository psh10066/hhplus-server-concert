package kr.hhplus.be.server.infrastructure.dao.user

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import kr.hhplus.be.server.domain.model.user.UserWallet
import kr.hhplus.be.server.infrastructure.dao.BaseEntity

@Entity
@Table(name = "user_wallet")
class UserWalletEntity(
    id: Long = 0,

    @Column(nullable = false, unique = true)
    val userId: Long,

    @Column(nullable = false)
    var balance: Long = 0L,
) : BaseEntity(id) {

    fun toModel(): UserWallet {
        return UserWallet(
            id = id,
            userId = userId,
            balance = balance
        )
    }

    companion object {
        fun from(userWallet: UserWallet): UserWalletEntity {
            return UserWalletEntity(
                id = userWallet.id,
                userId = userWallet.userId,
                balance = userWallet.balance
            )
        }
    }
}