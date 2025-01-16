package kr.hhplus.be.server.infrastructure.dao.reservation

import jakarta.persistence.*
import kr.hhplus.be.server.domain.model.reservation.Reservation
import kr.hhplus.be.server.domain.model.reservation.ReservationStatus
import kr.hhplus.be.server.infrastructure.dao.BaseEntity
import java.time.LocalDateTime

@Entity
@Table(name = "reservation")
class ReservationEntity(
    id: Long = 0,

    @Column(nullable = false)
    val concertScheduleId: Long,

    @Column(nullable = false)
    val concertSeatId: Long,

    @Column(nullable = false)
    val userId: Long,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: ReservationStatus,

    var expiredAt: LocalDateTime? = null,
) : BaseEntity(id) {

    fun toModel(): Reservation {
        return Reservation(
            id = id,
            concertScheduleId = concertScheduleId,
            concertSeatId = concertSeatId,
            userId = userId,
            status = status,
            expiredAt = expiredAt
        )
    }

    companion object {
        fun from(reservation: Reservation): ReservationEntity {
            return ReservationEntity(
                id = reservation.id,
                concertScheduleId = reservation.concertScheduleId,
                concertSeatId = reservation.concertSeatId,
                userId = reservation.userId,
                status = reservation.status,
                expiredAt = reservation.expiredAt
            )
        }
    }
}
