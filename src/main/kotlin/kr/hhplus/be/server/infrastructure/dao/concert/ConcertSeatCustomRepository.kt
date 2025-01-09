package kr.hhplus.be.server.infrastructure.dao.concert

import kr.hhplus.be.server.domain.model.concert.ConcertSeat

interface ConcertSeatCustomRepository {

    fun findAvailableSeats(concertScheduleId: Long): List<ConcertSeat>
}