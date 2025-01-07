package kr.hhplus.be.server.domain.model.queue

import java.util.*

interface QueueRepository {

    fun findByUserUuid(userUuid: UUID): Queue?

    fun save(queue: Queue): Queue
}