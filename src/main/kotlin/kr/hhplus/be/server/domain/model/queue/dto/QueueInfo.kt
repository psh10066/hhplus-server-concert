package kr.hhplus.be.server.domain.model.queue.dto

import kr.hhplus.be.server.domain.model.queue.Queue
import kr.hhplus.be.server.domain.model.queue.QueueStatus
import java.time.LocalDateTime
import java.util.*

data class QueueInfo(
    val id: Long,
    val userUuid: UUID,
    val status: QueueStatus,
    val expiredAt: LocalDateTime,
) {
    companion object {
        fun of(queue: Queue): QueueInfo {
            return QueueInfo(
                id = queue.id,
                userUuid = queue.userUuid,
                status = queue.status,
                expiredAt = queue.expiredAt
            )
        }
    }
}
