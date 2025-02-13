package kr.hhplus.be.server.domain.event

import kr.hhplus.be.server.domain.model.reservation.Reservation
import kr.hhplus.be.server.domain.model.user.User

data class ConcertReservationSucceedEvent(
    val user: User,
    val concertSeatId: Long,
    val reservation: Reservation
)