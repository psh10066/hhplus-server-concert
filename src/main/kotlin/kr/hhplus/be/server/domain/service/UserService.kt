package kr.hhplus.be.server.domain.service

import kr.hhplus.be.server.domain.model.user.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class UserService(
    private val userRepository: UserRepository,
    private val userWalletRepository: UserWalletRepository,
    private val userWalletHistoryRepository: UserWalletHistoryRepository
) {

    fun getUser(id: Long): User {
        return userRepository.getUserById(id)
    }

    fun getUser(uuid: UUID): User {
        return userRepository.getUserByUuid(uuid)
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

    @Transactional
    fun useBalance(userId: Long, amount: Long) {
        val userWallet = userWalletRepository.getByUserIdWithLock(userId)
        userWallet.use(amount)
        userWalletRepository.save(userWallet)
        userWalletHistoryRepository.save(UserWalletHistory(userWalletId = userWallet.id, amount = -amount))
    }
}