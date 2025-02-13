package kr.hhplus.be.server.support.client

import kr.hhplus.be.server.domain.service.UserService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Component
class UserApiClient(
    private val userService: UserService
) {
    private val log = LoggerFactory.getLogger(javaClass)

    // 외부 서비스 호출 가정이므로, 트랜잭션 분리
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun chargeBalance(userId: Long, amount: Long) {
        log.info("[외부 서비스 호출 가정] - 잔액 충전")
        userService.chargeBalance(userId, amount)
    }

    // 외부 서비스 호출 가정이므로, 트랜잭션 분리
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun useBalance(userId: Long, amount: Long) {
        log.info("[외부 서비스 호출 가정] - 잔액 사용")
        userService.useBalance(userId, amount)
    }
}
