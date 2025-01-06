package kr.hhplus.be.server.domain.service

import kr.hhplus.be.server.domain.model.user.UserRepository
import kr.hhplus.be.server.domain.model.user.UserWalletRepository
import kr.hhplus.be.server.domain.model.user.dto.UserInfo
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val userWalletRepository: UserWalletRepository
) {

    fun getUserInfo(id: Long): UserInfo {
        val user = userRepository.getUserById(id)
        return UserInfo.of(user)
    }

    fun getBalanceByUserId(userId: Long): Long {
        val userWallet = userWalletRepository.getByUserId(userId)
        return userWallet.balance
    }
}