package kr.hhplus.be.server.infrastructure.dao.user

import jakarta.persistence.LockModeType
import kr.hhplus.be.server.domain.model.user.UserWallet
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.transaction.annotation.Transactional

interface UserWalletJpaRepository : JpaRepository<UserWallet, Long> {
    fun findByUserId(userId: Long): UserWallet?

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    fun findForUpdateByUserId(userId: Long): UserWallet?
}