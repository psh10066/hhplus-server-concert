package kr.hhplus.be.server.domain.model.reservation

import jakarta.persistence.*
import kr.hhplus.be.server.infrastructure.dao.BaseEntity
import java.time.Clock
import java.time.LocalDateTime

@Entity
@Table(name = "reservation")
class Reservation(
    @Column(nullable = false)
    val concertScheduleId: Long,

    @Column(nullable = false)
    val concertSeatId: Long,

    @Column(nullable = false)
    val userId: Long,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val status: ReservationStatus,

    val expiredAt: LocalDateTime? = null,
) : BaseEntity() {

    companion object {
        fun book(clock: Clock, concertScheduleId: Long, concertSeatId: Long, userId: Long): Reservation {
            return Reservation(
                concertScheduleId = concertScheduleId,
                concertSeatId = concertSeatId,
                userId = userId,
                status = ReservationStatus.BOOKED,
                expiredAt = LocalDateTime.now(clock).plusMinutes(5)
            )
        }
    }

    fun isBooked(clock: Clock): Boolean {
        if (status == ReservationStatus.PAYMENT_COMPLETED) {
            return true
        }
        if (expiredAt?.isAfter(LocalDateTime.now(clock)) == true) {
            return true
        }
        return false
    }
}
