package kr.hhplus.be.server.domain.event

import kr.hhplus.be.server.domain.model.user.User

data class ConcertReservationFinishedEvent(
    val user: User,
    val concertSeatId: Long
)