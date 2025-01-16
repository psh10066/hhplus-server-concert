package kr.hhplus.be.server.domain.model.reservation

import kr.hhplus.be.server.support.error.CustomException
import kr.hhplus.be.server.support.error.ErrorType
import java.time.Clock
import java.time.LocalDateTime

class Reservation(
    val id: Long = 0,
    val concertSeatId: Long,
    val userId: Long,
    var status: ReservationStatus,
    var expiredAt: LocalDateTime? = null,
) {

    companion object {
        fun book(clock: Clock, concertSeatId: Long, userId: Long): Reservation {
            return Reservation(
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
            throw CustomException(ErrorType.NOT_PAYABLE_RESERVATION)
        }
        status = ReservationStatus.PAYMENT_COMPLETED
        expiredAt = null
    }
}
