package kr.hhplus.be.server.domain.model.user

class UserWallet(
    val id: Long = 0,
    val userId: Long,
    var balance: Long = 0L,
) {

    fun charge(amount: Long) {
        require(amount > 0) { "잘못된 충전 금액입니다." }

        balance += amount
    }

    fun use(amount: Long) {
        require(amount > 0) { "잘못된 사용 금액입니다." }
        require(balance >= amount) { "잔액이 부족합니다." }

        balance -= amount
    }
}