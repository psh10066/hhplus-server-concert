package kr.hhplus.be.server.infrastructure.dao.queue

import kr.hhplus.be.server.domain.model.queue.Queue
import kr.hhplus.be.server.domain.model.queue.QueueRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
class QueueRepositoryImpl(
    private val queueJpaRepository: QueueJpaRepository
) : QueueRepository {

    override fun findNotExpiredByUserUuid(userUuid: UUID): Queue? {
        return queueJpaRepository.findNotExpiredByUserUuid(userUuid)?.toModel()
    }

    override fun save(queue: Queue): Queue {
        return queueJpaRepository.save(QueueEntity.from(queue)).toModel()
    }

    override fun getNotExpiredWithOrder(count: Int): List<Queue> {
        return queueJpaRepository.getNotExpiredWithOrder(count).map {
            it.toModel()
        }
    }

    override fun findNotExpiredByToken(token: String): Queue? {
        return queueJpaRepository.findNotExpiredByToken(token)?.toModel()
    }

    override fun deleteByUserUuid(userUuid: UUID) {
        queueJpaRepository.deleteByUserUuid(userUuid)
    }
}