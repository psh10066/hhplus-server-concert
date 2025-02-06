package kr.hhplus.be.server.api.controller.v1.response

import kr.hhplus.be.server.domain.model.queue.QueueStatus

data class GetQueueRankResponse(
    val status: QueueStatus,
    val rank: Long?
)