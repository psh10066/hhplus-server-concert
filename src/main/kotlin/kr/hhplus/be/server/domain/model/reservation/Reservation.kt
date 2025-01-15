package kr.hhplus.be.server.domain.model.reservation

import java.time.Clock
import java.time.LocalDateTime

class Reservation(
    val id: Long = 0,
    val concertScheduleId: Long,
    val concertSeatId: Long,
    val userId: Long,
    var status: ReservationStatus,
    var expiredAt: LocalDateTime? = null,
) {

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

    fun pay(clock: Clock) {
        if (status != ReservationStatus.BOOKED || expiredAt?.isAfter(LocalDateTime.now(clock)) != true) {
            throw IllegalStateException("결제 가능한 예약이 아닙니다.")
        }
        status = ReservationStatus.PAYMENT_COMPLETED
        expiredAt = null
    }
}
