package kr.hhplus.be.server.domain.model.concert

class ConcertSeat(
    val id: Long = 0,
    val concertId: Long,
    val seatNumber: Int,
)