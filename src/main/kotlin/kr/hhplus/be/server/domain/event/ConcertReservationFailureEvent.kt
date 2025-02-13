package kr.hhplus.be.server.domain.event

import kr.hhplus.be.server.domain.model.user.User

data class ConcertReservationFailureEvent(
    val user: User,
    val concertSeatId: Long
)