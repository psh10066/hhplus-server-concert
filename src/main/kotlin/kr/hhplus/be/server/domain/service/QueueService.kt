package kr.hhplus.be.server.domain.service

import kr.hhplus.be.server.domain.model.queue.ACTIVE_TOKEN_COUNT
import kr.hhplus.be.server.domain.model.queue.Queue
import kr.hhplus.be.server.domain.model.queue.QueueRepository
import kr.hhplus.be.server.domain.model.queue.QueueStatus
import kr.hhplus.be.server.domain.model.queue.dto.QueueInfo
import org.springframework.stereotype.Service
import java.time.Clock
import java.util.*

@Service
class QueueService(
    private val clock: Clock,
    private val queueRepository: QueueRepository
) {

    fun issueToken(userUuid: UUID): String {
        val queue = queueRepository.findByUserUuid(userUuid) ?: run {
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

    fun getActiveQueue(token: String): QueueInfo {
        val queue = queueRepository.findNotExpiredByToken(token)
        if (queue?.isActive() != true) {
            throw IllegalArgumentException("활성화된 대기열이 아닙니다.")
        }
        return QueueInfo.of(queue)
    }

    fun readyPayment(token: String) {
        val queue = queueRepository.findNotExpiredByToken(token)
        if (queue?.isActive() != true) {
            throw IllegalArgumentException("활성화된 대기열이 아닙니다.")
        }
        queue.readyPayment(clock)
        queueRepository.save(queue)
    }

    fun expire(id: Long) {
        queueRepository.deleteById(id)
    }
}