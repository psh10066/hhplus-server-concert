package kr.hhplus.be.server.infrastructure.dao.concert

interface ConcertSeatCustomRepository {

    fun findAvailableSeats(concertScheduleId: Long): List<ConcertSeatEntity>
}