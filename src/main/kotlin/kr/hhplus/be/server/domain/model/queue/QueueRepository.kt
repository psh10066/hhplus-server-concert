package kr.hhplus.be.server.domain.model.queue

import java.util.*

interface QueueRepository {

    fun findNotExpiredByUserUuid(userUuid: UUID): Queue?

    fun save(queue: Queue): Queue

    fun getNotExpiredWithOrder(count: Int): List<Queue>

    fun findNotExpiredByToken(token: String): Queue?

    fun deleteByUserUuid(userUuid: UUID)
}