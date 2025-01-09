package kr.hhplus.be.server.infrastructure.dao.concert

import kr.hhplus.be.server.domain.model.concert.ConcertSchedule
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface ConcertScheduleJpaRepository : JpaRepository<ConcertSchedule, Long> {
    fun findByConcertIdAndStartTimeAfter(concertId: Long, startTime: LocalDateTime): List<ConcertSchedule>
}