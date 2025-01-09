package kr.hhplus.be.server.domain.service

import kr.hhplus.be.server.domain.model.user.UserRepository
import kr.hhplus.be.server.domain.model.user.UserWalletHistory
import kr.hhplus.be.server.domain.model.user.UserWalletHistoryRepository
import kr.hhplus.be.server.domain.model.user.UserWalletRepository
import kr.hhplus.be.server.domain.model.user.dto.UserInfo
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class UserService(
    private val userRepository: UserRepository,
    private val userWalletRepository: UserWalletRepository,
    private val userWalletHistoryRepository: UserWalletHistoryRepository
) {

    fun getUserInfo(id: Long): UserInfo {
        val user = userRepository.getUserById(id)
        return UserInfo.of(user)
    }

    fun getUserInfo(uuid: UUID): UserInfo {
        val user = userRepository.getUserByUuid(uuid)
        return UserInfo.of(user)
    }

    fun getBalanceByUserId(userId: Long): Long {
        val userWallet = userWalletRepository.getByUserId(userId)
        return userWallet.balance
    }

    @Transactional
    fun chargeBalance(userId: Long, amount: Long) {
        val userWallet = userWalletRepository.getByUserIdWithLock(userId)
        userWallet.charge(amount)
        userWalletRepository.save(userWallet)
        userWalletHistoryRepository.save(UserWalletHistory(userWalletId = userWallet.id, amount = amount))
    }
}