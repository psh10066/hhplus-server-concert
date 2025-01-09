package kr.hhplus.be.server.infrastructure.dao.concert

import kr.hhplus.be.server.domain.model.concert.ConcertSchedule
import kr.hhplus.be.server.domain.model.concert.ConcertScheduleRepository
import org.springframework.stereotype.Component

@Component
class ConcertScheduleRepositoryImpl(
    private val concertScheduleJpaRepository: ConcertScheduleJpaRepository
) : ConcertScheduleRepository {

    override fun findByConcertId(concertId: Long): List<ConcertSchedule> {
        return concertScheduleJpaRepository.findByConcertId(concertId)
    }
}