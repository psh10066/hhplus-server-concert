package kr.hhplus.be.server.domain.model.concert

import kr.hhplus.be.server.support.error.CustomException
import kr.hhplus.be.server.support.error.ErrorType

class ConcertSeat(
    val id: Long = 0,
    val concertScheduleId: Long,
    val seatNumber: Int,
    var status: ConcertSeatStatus = ConcertSeatStatus.AVAILABLE,
    var version: Long = 0,
) {
    fun reserve() {
        if (status != ConcertSeatStatus.AVAILABLE) {
            throw CustomException(ErrorType.ALREADY_RESERVED_CONCERT_SEAT)
        }
        status = ConcertSeatStatus.RESERVED
    }

    fun completePayment() {
        if (status != ConcertSeatStatus.RESERVED) {
            throw CustomException(ErrorType.NOT_PAYABLE_RESERVATION)
        }
        status = ConcertSeatStatus.PAYMENT_COMPLETED
    }

    fun rollbackPayment() {
        if (status != ConcertSeatStatus.PAYMENT_COMPLETED) {
            throw CustomException(ErrorType.CANNOT_ROLLBACK_PAY_RESERVATION)
        }
        status = ConcertSeatStatus.RESERVED
    }

    fun cancelReservation() {
        status = ConcertSeatStatus.AVAILABLE
    }
}