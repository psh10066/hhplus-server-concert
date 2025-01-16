package kr.hhplus.be.server.infrastructure.dao.concert

import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface ConcertScheduleJpaRepository : JpaRepository<ConcertScheduleEntity, Long> {
    fun findByConcertIdAndStartTimeAfter(concertId: Long, startTime: LocalDateTime): List<ConcertScheduleEntity>
}