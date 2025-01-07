package kr.hhplus.be.server.domain.model.user

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import kr.hhplus.be.server.infrastructure.dao.BaseEntity

@Entity
@Table(name = "user_wallet")
class UserWallet(
    @Column(nullable = false, unique = true)
    val userId: Long,

    @Column(nullable = false)
    var balance: Long = 0L,
) : BaseEntity() {

    fun charge(amount: Long) {
        require(amount > 0) { "잘못된 충전 금액입니다." }

        balance += amount
    }
}