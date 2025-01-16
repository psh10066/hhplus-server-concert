package kr.hhplus.be.server.infrastructure.dao.queue

import java.util.*

interface QueueCustomRepository {

    fun findNotExpiredByUserUuid(userUuid: UUID): QueueEntity?

    fun getNotExpiredWithOrder(count: Int): List<QueueEntity>

    fun findNotExpiredByToken(token: String): QueueEntity?
}