package kr.hhplus.be.server.domain.model.concert

interface ConcertScheduleRepository {

    fun findByConcertId(concertId: Long): List<ConcertSchedule>
}