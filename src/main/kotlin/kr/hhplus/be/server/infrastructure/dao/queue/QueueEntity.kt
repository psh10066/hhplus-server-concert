package kr.hhplus.be.server.infrastructure.dao.queue

import jakarta.persistence.*
import kr.hhplus.be.server.domain.model.queue.Queue
import kr.hhplus.be.server.domain.model.queue.QueueStatus
import kr.hhplus.be.server.infrastructure.dao.BaseEntity
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "queue")
class QueueEntity(
    id: Long = 0,

    @Column(nullable = false, unique = true)
    val userUuid: UUID,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: QueueStatus,

    @Column(nullable = false)
    var token: String,

    @Column(nullable = false)
    var expiredAt: LocalDateTime,
) : BaseEntity(id) {

    fun toModel(): Queue {
        return Queue(
            id = id,
            userUuid = userUuid,
            status = status,
            token = token,
            expiredAt = expiredAt
        )
    }

    companion object {
        fun from(queue: Queue): QueueEntity {
            return QueueEntity(
                id = queue.id,
                userUuid = queue.userUuid,
                status = queue.status,
                token = queue.token,
                expiredAt = queue.expiredAt
            )
        }
    }
}