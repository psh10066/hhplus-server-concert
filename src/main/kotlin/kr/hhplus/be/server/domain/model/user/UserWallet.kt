package kr.hhplus.be.server.domain.model.user

import kr.hhplus.be.server.support.error.CustomException
import kr.hhplus.be.server.support.error.ErrorType

class UserWallet(
    val id: Long = 0,
    val userId: Long,
    var balance: Long = 0L,
) {

    fun charge(amount: Long) {
        require(amount > 0) { throw CustomException(ErrorType.INVALID_CHARGE_AMOUNT) }

        balance += amount
    }

    fun use(amount: Long) {
        require(amount > 0) { throw CustomException(ErrorType.INVALID_USAGE_AMOUNT) }
        require(balance >= amount) { throw CustomException(ErrorType.LACK_OF_BALANCE) }

        balance -= amount
    }
}