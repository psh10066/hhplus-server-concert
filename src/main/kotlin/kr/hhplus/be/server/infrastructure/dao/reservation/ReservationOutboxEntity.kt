package kr.hhplus.be.server.infrastructure.dao.reservation

import jakarta.persistence.*
import kr.hhplus.be.server.domain.model.reservation.ReservationOutbox
import kr.hhplus.be.server.domain.model.reservation.ReservationOutboxStatus
import kr.hhplus.be.server.infrastructure.dao.BaseEntity

@Entity
@Table(name = "reservation_outbox")
class ReservationOutboxEntity(
    id: Long = 0,

    @Column(nullable = false)
    val operationType: String,

    @Column(nullable = false)
    val operationId: String,

    @Column(nullable = false)
    val topic: String,

    @Column(name = "topic_key") // 'key'가 MySQL 예약어이므로 변경 필요
    val key: String?,

    @Lob
    @Column(nullable = false)
    val message: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: ReservationOutboxStatus,
) : BaseEntity(id) {

    fun toModel(): ReservationOutbox {
        return ReservationOutbox(
            id = id,
            operationType = operationType,
            operationId = operationId,
            topic = topic,
            key = key,
            message = message,
            status = status
        )
    }

    companion object {
        fun from(reservationOutbox: ReservationOutbox): ReservationOutboxEntity {
            return ReservationOutboxEntity(
                id = reservationOutbox.id,
                operationType = reservationOutbox.operationType,
                operationId = reservationOutbox.operationId,
                topic = reservationOutbox.topic,
                key = reservationOutbox.key,
                message = reservationOutbox.message,
                status = reservationOutbox.status
            )
        }
    }
}
