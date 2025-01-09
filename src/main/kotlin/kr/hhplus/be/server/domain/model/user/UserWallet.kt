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

    fun use(amount: Long) {
        require(amount > 0) { "잘못된 사용 금액입니다." }
        require(balance >= amount) { "잔액이 부족합니다." }

        balance -= amount
    }
}