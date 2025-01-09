package kr.hhplus.be.server.domain.model.concert

interface ConcertSeatRepository {

    fun findAvailableSeats(concertScheduleId: Long): List<ConcertSeat>
}