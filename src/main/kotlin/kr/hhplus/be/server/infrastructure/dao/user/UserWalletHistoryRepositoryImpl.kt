package kr.hhplus.be.server.infrastructure.dao.user

import kr.hhplus.be.server.domain.model.user.UserWalletHistory
import kr.hhplus.be.server.domain.model.user.UserWalletHistoryRepository
import org.springframework.stereotype.Component

@Component
class UserWalletHistoryRepositoryImpl(
    private val userWalletHistoryJpaRepository: UserWalletHistoryJpaRepository
) : UserWalletHistoryRepository {

    override fun save(userWalletHistory: UserWalletHistory): UserWalletHistory {
        return userWalletHistoryJpaRepository.save(UserWalletHistoryEntity.from(userWalletHistory)).toModel()
    }
}