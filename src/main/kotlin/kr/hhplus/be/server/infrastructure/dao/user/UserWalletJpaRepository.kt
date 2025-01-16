package kr.hhplus.be.server.infrastructure.dao.user

import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.transaction.annotation.Transactional

interface UserWalletJpaRepository : JpaRepository<UserWalletEntity, Long> {
    fun findByUserId(userId: Long): UserWalletEntity?

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    fun findForUpdateByUserId(userId: Long): UserWalletEntity?
}