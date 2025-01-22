package kr.hhplus.be.server.support.aop.lock

import kr.hhplus.be.server.support.error.CustomException
import kr.hhplus.be.server.support.error.ErrorType
import kr.hhplus.be.server.support.util.CustomSpringELParser
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.redisson.api.RLock
import org.redisson.api.RedissonClient
import org.slf4j.LoggerFactory
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Order(0)
@Aspect
@Component
class DistributedLockAspect(
    private val redissonClient: RedissonClient,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    companion object {
        private const val LOCK_PREFIX = "LOCK:"
    }

    @Around("@annotation(kr.hhplus.be.server.support.aop.lock.DistributedLock)")
    fun lock(joinPoint: ProceedingJoinPoint): Any? {
        val signature = joinPoint.signature as MethodSignature
        val method = signature.method
        val distributedLock = method.getAnnotation(DistributedLock::class.java)

        val key = LOCK_PREFIX + CustomSpringELParser.getByMethodArgument(
            parameterNames = signature.parameterNames,
            args = joinPoint.args,
            key = distributedLock.key
        )

        val rLock: RLock = redissonClient.getLock(key)
        val isLocked = rLock.tryLock(distributedLock.waitTime, distributedLock.leaseTime, distributedLock.timeUnit)
        if (!isLocked) {
            log.error("[DistributedLock] key=[$key] Lock 획득 실패")
            throw CustomException(ErrorType.DEFAULT_ERROR)
        }

        log.debug("[DistributedLock] key=[$key] Lock 획득 성공")
        try {
            return joinPoint.proceed()
        } finally {
            try {
                rLock.unlock()
                log.debug("[DistributedLock] key=[$key] Lock 반환 성공")
            } catch (e: IllegalMonitorStateException) {
                log.error("[DistributedLock] key=[$key] Lock 이미 반환됨")
            }
        }
    }
}