package kr.hhplus.be.server.domain.model.concert

interface ConcertScheduleRepository {

    fun findByConcertId(concertId: Long): List<ConcertSchedule>

    fun getById(id: Long): ConcertSchedule
}