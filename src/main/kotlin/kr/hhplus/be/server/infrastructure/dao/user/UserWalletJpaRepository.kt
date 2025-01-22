package kr.hhplus.be.server.infrastructure.dao.user

import org.springframework.data.jpa.repository.JpaRepository

interface UserWalletJpaRepository : JpaRepository<UserWalletEntity, Long> {
    fun findByUserId(userId: Long): UserWalletEntity?
}