package kr.hhplus.be.server.domain.service

import kr.hhplus.be.server.domain.model.queue.Queue
import kr.hhplus.be.server.domain.model.queue.QueueRepository
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
}