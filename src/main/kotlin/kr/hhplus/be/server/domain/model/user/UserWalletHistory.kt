package kr.hhplus.be.server.domain.model.user

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import kr.hhplus.be.server.infrastructure.dao.BaseEntity

@Entity
@Table(name = "user_wallet_history")
class UserWalletHistory(
    @Column(nullable = false, unique = true)
    val userWalletId: Long,

    @Column(nullable = false)
    var amount: Long,
) : BaseEntity()