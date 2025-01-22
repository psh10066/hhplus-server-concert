package kr.hhplus.be.server.infrastructure.dao.user

import kr.hhplus.be.server.domain.model.user.UserWallet
import kr.hhplus.be.server.domain.model.user.UserWalletRepository
import kr.hhplus.be.server.support.error.CustomException
import kr.hhplus.be.server.support.error.ErrorType
import org.springframework.stereotype.Component

@Component
class UserWalletRepositoryImpl(
    private val userWalletJpaRepository: UserWalletJpaRepository
) : UserWalletRepository {

    override fun getByUserId(userId: Long): UserWallet {
        return userWalletJpaRepository.findByUserId(userId)?.toModel()
            ?: throw CustomException(ErrorType.USER_WALLET_NOT_FOUND)
    }

    override fun save(userWallet: UserWallet): UserWallet {
        return userWalletJpaRepository.save(UserWalletEntity.from(userWallet)).toModel()
    }
}