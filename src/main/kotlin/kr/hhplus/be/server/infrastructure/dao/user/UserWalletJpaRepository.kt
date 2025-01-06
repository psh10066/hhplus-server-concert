package kr.hhplus.be.server.infrastructure.dao.user

import kr.hhplus.be.server.domain.model.user.UserWallet
import org.springframework.data.jpa.repository.JpaRepository

interface UserWalletJpaRepository : JpaRepository<UserWallet, Long> {
    fun findByUserId(userId: Long): UserWallet?
}