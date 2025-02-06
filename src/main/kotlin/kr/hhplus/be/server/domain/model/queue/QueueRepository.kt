package kr.hhplus.be.server.domain.model.queue

import java.util.*
import kotlin.time.Duration

interface QueueRepository {

    fun addWaitingToken(token: String, score: Double)

    fun findWaitingTokenRank(token: String): Long?

    fun existActiveToken(token: String): Boolean

    fun activateTokens(count: Long, ttl: Duration)

    fun updateActiveTokenTTLByUserUuid(userUuid: UUID, ttl: Duration)

    fun deleteActiveTokenByUserUuid(userUuid: UUID)
}