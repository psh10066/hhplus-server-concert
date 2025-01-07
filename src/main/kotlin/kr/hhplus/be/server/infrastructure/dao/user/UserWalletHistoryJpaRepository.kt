package kr.hhplus.be.server.infrastructure.dao.user

import kr.hhplus.be.server.domain.model.user.UserWalletHistory
import org.springframework.data.jpa.repository.JpaRepository

interface UserWalletHistoryJpaRepository : JpaRepository<UserWalletHistory, Long>