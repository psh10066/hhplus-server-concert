package kr.hhplus.be.server.infrastructure.dao.queue

import kr.hhplus.be.server.domain.model.queue.Queue
import kr.hhplus.be.server.domain.model.queue.QueueRepository
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.time.Duration

const val WAITING_TOKENS_KEY = "waiting-tokens"
const val ACTIVE_TOKEN_PREFIX = "active-token:"

@Component
class QueueRedisRepository(
    private val redisTemplate: StringRedisTemplate
) : QueueRepository {

    private fun getActiveTokenKey(userUuid: UUID): String {
        return ACTIVE_TOKEN_PREFIX + userUuid
    }

    override fun addWaitingToken(token: String, score: Double) {
        redisTemplate.opsForZSet().add(WAITING_TOKENS_KEY, token, score)
    }

    override fun findWaitingTokenRank(token: String): Long? {
        return redisTemplate.opsForZSet().rank(WAITING_TOKENS_KEY, token)
    }

    override fun existActiveToken(token: String): Boolean {
        val key = getActiveTokenKey(Queue.parseUserUuid(token))
        return redisTemplate.opsForValue()[key]
            ?.let { it == token }
            ?: false
    }

    override fun activateTokens(count: Long, ttl: Duration) {
        val start = 0L
        val end = count - 1

        redisTemplate.opsForZSet().range(WAITING_TOKENS_KEY, start, end)?.let { tokens ->
            tokens.forEach { token ->
                val userUuid = Queue.parseUserUuid(token)
                val key = getActiveTokenKey(userUuid)
                redisTemplate.opsForValue().set(key, token, ttl.inWholeSeconds, TimeUnit.SECONDS)
            }
            redisTemplate.opsForZSet().removeRange(WAITING_TOKENS_KEY, start, end)
        }
    }

    override fun updateActiveTokenTTLByUserUuid(userUuid: UUID, ttl: Duration) {
        val key = getActiveTokenKey(userUuid)
        redisTemplate.expire(key, ttl.inWholeSeconds, TimeUnit.SECONDS)
    }

    override fun deleteActiveTokenByUserUuid(userUuid: UUID) {
        val key = getActiveTokenKey(userUuid)
        redisTemplate.delete(key)
    }
}