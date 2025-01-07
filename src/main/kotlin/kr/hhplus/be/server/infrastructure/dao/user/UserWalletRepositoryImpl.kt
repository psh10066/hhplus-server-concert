package kr.hhplus.be.server.infrastructure.dao.user

import kr.hhplus.be.server.domain.model.user.UserWallet
import kr.hhplus.be.server.domain.model.user.UserWalletRepository
import org.springframework.stereotype.Component

@Component
class UserWalletRepositoryImpl(
    private val userWalletJpaRepository: UserWalletJpaRepository
) : UserWalletRepository {

    override fun getByUserId(userId: Long): UserWallet {
        return userWalletJpaRepository.findByUserId(userId)
            ?: throw IllegalStateException("존재하지 않는 지갑입니다.")
    }

    override fun getByUserIdWithLock(userId: Long): UserWallet {
        return userWalletJpaRepository.findForUpdateByUserId(userId)
            ?: throw IllegalStateException("존재하지 않는 지갑입니다.")
    }

    override fun save(userWallet: UserWallet): UserWallet {
        return userWalletJpaRepository.save(userWallet)
    }
}