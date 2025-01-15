package kr.hhplus.be.server.infrastructure.dao.queue

interface QueueCustomRepository {

    fun getNotExpiredWithOrder(count: Int): List<QueueEntity>

    fun findNotExpiredByToken(token: String): QueueEntity?
}