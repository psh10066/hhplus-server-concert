package kr.hhplus.be.server.infrastructure.dao.queue

import kr.hhplus.be.server.domain.model.queue.Queue
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface QueueJpaRepository : JpaRepository<Queue, Long>, QueueCustomRepository {
    fun findByUserUuid(userUuid: UUID): Queue?
}