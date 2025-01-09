package kr.hhplus.be.server.infrastructure.dao.concert

import kr.hhplus.be.server.domain.model.concert.ConcertSeat
import kr.hhplus.be.server.domain.model.concert.ConcertSeatRepository
import org.springframework.stereotype.Component

@Component
class ConcertSeatRepositoryImpl(
    private val concertSeatJpaRepository: ConcertSeatJpaRepository
) : ConcertSeatRepository {

    override fun findAvailableSeats(concertScheduleId: Long): List<ConcertSeat> {
        return concertSeatJpaRepository.findAvailableSeats(concertScheduleId)
    }
}