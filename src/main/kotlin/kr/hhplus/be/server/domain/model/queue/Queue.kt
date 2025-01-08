package kr.hhplus.be.server.domain.model.queue

import jakarta.persistence.*
import kr.hhplus.be.server.infrastructure.dao.BaseEntity
import java.time.Clock
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "queue")
class Queue(
    @Column(nullable = false, unique = true)
    val userUuid: UUID,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: QueueStatus,

    @Column(nullable = false)
    var token: String,

    @Column(nullable = false)
    val expiredAt: LocalDateTime,
) : BaseEntity() {

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
}