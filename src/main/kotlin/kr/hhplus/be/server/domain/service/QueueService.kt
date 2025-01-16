package kr.hhplus.be.server.domain.service

import kr.hhplus.be.server.domain.model.queue.ACTIVE_TOKEN_COUNT
import kr.hhplus.be.server.domain.model.queue.Queue
import kr.hhplus.be.server.domain.model.queue.QueueRepository
import kr.hhplus.be.server.domain.model.queue.QueueStatus
import kr.hhplus.be.server.support.error.CustomException
import kr.hhplus.be.server.support.error.ErrorType
import org.springframework.stereotype.Service
import java.time.Clock
import java.util.*

@Service
class QueueService(
    private val clock: Clock,
    private val queueRepository: QueueRepository
) {

    fun issueToken(userUuid: UUID): String {
        val queue = queueRepository.findNotExpiredByUserUuid(userUuid) ?: run {
            val createdQueue = Queue.create(clock, userUuid)
            queueRepository.save(createdQueue)
        }
        return queue.token
    }

    fun activate() {
        queueRepository.getNotExpiredWithOrder(ACTIVE_TOKEN_COUNT)
            .filter { queue -> queue.status == QueueStatus.WAITING }
            .forEach { queue ->
                queue.activate()
                queueRepository.save(queue)
            }
    }

    fun getActiveQueue(token: String): Queue {
        val queue = queueRepository.findNotExpiredByToken(token)
        if (queue?.isActive() != true) {
            throw CustomException(ErrorType.NOT_AN_ACTIVE_QUEUE)
        }
        return queue
    }

    fun readyPayment(token: String) {
        val queue = queueRepository.findNotExpiredByToken(token)
        if (queue?.isActive() != true) {
            throw CustomException(ErrorType.NOT_AN_ACTIVE_QUEUE)
        }
        queue.readyPayment(clock)
        queueRepository.save(queue)
    }

    fun expire(id: Long) {
        queueRepository.deleteById(id)
    }
}