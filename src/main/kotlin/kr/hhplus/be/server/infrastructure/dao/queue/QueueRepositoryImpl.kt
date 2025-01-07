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
}