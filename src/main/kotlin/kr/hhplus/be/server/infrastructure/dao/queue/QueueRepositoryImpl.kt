package kr.hhplus.be.server.infrastructure.dao.queue

import kr.hhplus.be.server.domain.model.queue.Queue
import kr.hhplus.be.server.domain.model.queue.QueueRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
class QueueRepositoryImpl(
    private val queueJpaRepository: QueueJpaRepository
) : QueueRepository {

    override fun findByUserUuid(userUuid: UUID): Queue? {
        return queueJpaRepository.findByUserUuid(userUuid)
    }

    override fun save(queue: Queue): Queue {
        return queueJpaRepository.save(queue)
    }

    override fun getNotExpiredWithOrder(count: Int): List<Queue> {
        return queueJpaRepository.getNotExpiredWithOrder(count)
    }

    override fun findNotExpiredByToken(token: String): Queue? {
        return queueJpaRepository.findNotExpiredByToken(token)
    }
}