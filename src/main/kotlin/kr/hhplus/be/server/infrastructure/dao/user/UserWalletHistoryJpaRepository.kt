package kr.hhplus.be.server.infrastructure.dao.user

import org.springframework.data.jpa.repository.JpaRepository

interface UserWalletHistoryJpaRepository : JpaRepository<UserWalletHistoryEntity, Long>