package kr.hhplus.be.server.infrastructure.dao.concert

import kr.hhplus.be.server.domain.model.concert.ConcertSchedule
import org.springframework.data.jpa.repository.JpaRepository

interface ConcertScheduleJpaRepository : JpaRepository<ConcertSchedule, Long> {
    fun findByConcertId(concertId: Long): List<ConcertSchedule>
}