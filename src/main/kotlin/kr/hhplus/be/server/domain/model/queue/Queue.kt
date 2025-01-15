package kr.hhplus.be.server.domain.model.queue

import java.time.Clock
import java.time.LocalDateTime
import java.util.*

class Queue(
    val id: Long = 0,
    val userUuid: UUID,
    var status: QueueStatus,
    var token: String,
    var expiredAt: LocalDateTime,
) {

    companion object {
        fun create(clock: Clock, userUuid: UUID): Queue {
            val status = QueueStatus.WAITING
            val expiredAt = LocalDateTime.now(clock).plusMinutes(10L)

            return Queue(
                userUuid = userUuid,
                status = status,
                token = "$userUuid|$status|$expiredAt",
                expiredAt = expiredAt
            )
        }
    }

    fun activate() {
        status = QueueStatus.ACTIVE
        token = "$userUuid|$status|$expiredAt"
    }

    fun isActive(): Boolean {
        return status == QueueStatus.ACTIVE
    }

    fun readyPayment(clock: Clock) {
        expiredAt = LocalDateTime.now(clock).plusMinutes(5L)
    }
}