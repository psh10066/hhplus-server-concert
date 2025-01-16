package kr.hhplus.be.server.domain.model.concert

class ConcertSeat(
    val id: Long = 0,
    val concertScheduleId: Long,
    val seatNumber: Int,
)