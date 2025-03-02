package kr.hhplus.be.server.domain.model.concert

interface ConcertSeatRepository {

    fun findAvailableSeats(concertScheduleId: Long): List<ConcertSeat>

    fun getById(id: Long): ConcertSeat

    fun saveAndFlush(concertSeat: ConcertSeat): ConcertSeat

    fun getAllById(concertSeatIds: List<Long>): List<ConcertSeat>
}