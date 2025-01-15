package kr.hhplus.be.server.infrastructure.dao.queue

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface QueueJpaRepository : JpaRepository<QueueEntity, Long>, QueueCustomRepository {
    fun findByUserUuid(userUuid: UUID): QueueEntity?
}