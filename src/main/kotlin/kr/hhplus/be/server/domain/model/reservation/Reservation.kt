package kr.hhplus.be.server.domain.model.reservation

import kr.hhplus.be.server.support.error.CustomException
import kr.hhplus.be.server.support.error.ErrorType
import java.time.Clock
import java.time.LocalDateTime

class Reservation(
    val id: Long = 0,
    val concertId: Long,
    val concertSeatId: Long,
    val userId: Long,
    var status: ReservationStatus,
    var expiredAt: LocalDateTime? = null,
) {

    companion object {
        fun reserve(clock: Clock, concertId: Long, concertSeatId: Long, userId: Long): Reservation {
            return Reservation(
                concertId = concertId,
                concertSeatId = concertSeatId,
                userId = userId,
                status = ReservationStatus.RESERVED,
                expiredAt = LocalDateTime.now(clock).plusMinutes(5)
            )
        }
    }

    fun isReserved(clock: Clock): Boolean {
        if (status == ReservationStatus.PAYMENT_COMPLETED) {
            return true
        }
        if (expiredAt?.isAfter(LocalDateTime.now(clock)) == true) {
            return true
        }
        return false
    }

    fun pay(clock: Clock) {
        if (status != ReservationStatus.RESERVED || expiredAt?.isAfter(LocalDateTime.now(clock)) != true) {
            throw CustomException(ErrorType.NOT_PAYABLE_RESERVATION)
        }
        status = ReservationStatus.PAYMENT_COMPLETED
        expiredAt = null
    }

    fun rollbackPay(expiredAt: LocalDateTime) {
        if (status != ReservationStatus.PAYMENT_COMPLETED) {
            throw CustomException(ErrorType.CANNOT_ROLLBACK_PAY_RESERVATION)
        }
        status = ReservationStatus.RESERVED
        this.expiredAt = expiredAt
    }
}
