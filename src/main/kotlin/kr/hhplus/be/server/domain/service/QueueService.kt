package kr.hhplus.be.server.domain.service

import kr.hhplus.be.server.domain.model.queue.Queue
import kr.hhplus.be.server.domain.model.queue.QueueRank
import kr.hhplus.be.server.domain.model.queue.QueueRepository
import kr.hhplus.be.server.support.error.CustomException
import kr.hhplus.be.server.support.error.ErrorType
import org.springframework.stereotype.Service
import java.time.Clock
import java.util.*
import kotlin.time.Duration.Companion.minutes

@Service
class QueueService(
    private val clock: Clock,
    private val queueRepository: QueueRepository
) {

    fun issueToken(userUuid: UUID): String {
        val queue = Queue.create(userUuid)
        queueRepository.addWaitingToken(queue.token, clock.millis().toDouble())
        return queue.token
    }

    fun getQueueRank(token: String): QueueRank {
        queueRepository.findWaitingTokenRank(token)?.let {
            return QueueRank.waiting(it)
        }
        if (queueRepository.existActiveToken(token)) {
            return QueueRank.active()
        }
        throw CustomException(ErrorType.UNAUTHORIZED)
    }

    fun activate(count: Long) {
        queueRepository.activateTokens(count, 10.minutes)
    }

    fun getActiveQueue(token: String): Queue {
        if (!queueRepository.existActiveToken(token)) {
            throw CustomException(ErrorType.NOT_AN_ACTIVE_QUEUE)
        }
        return Queue.of(token)
    }

    fun readyPayment(userUuid: UUID) {
        queueRepository.updateActiveTokenTTLByUserUuid(userUuid, 5.minutes)
    }

    fun expire(userUuid: UUID) {
        queueRepository.deleteActiveTokenByUserUuid(userUuid)
    }
}