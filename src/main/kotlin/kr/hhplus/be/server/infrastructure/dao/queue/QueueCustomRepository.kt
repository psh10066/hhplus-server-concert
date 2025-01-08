package kr.hhplus.be.server.infrastructure.dao.queue

import kr.hhplus.be.server.domain.model.queue.Queue

interface QueueCustomRepository {

    fun getNotExpiredWithOrder(count: Int): List<Queue>
}