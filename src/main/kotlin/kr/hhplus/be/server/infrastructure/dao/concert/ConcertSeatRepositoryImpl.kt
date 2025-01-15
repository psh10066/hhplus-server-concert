package kr.hhplus.be.server.infrastructure.dao.concert

import kr.hhplus.be.server.domain.model.concert.ConcertSeat
import kr.hhplus.be.server.domain.model.concert.ConcertSeatRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class ConcertSeatRepositoryImpl(
    private val concertSeatJpaRepository: ConcertSeatJpaRepository
) : ConcertSeatRepository {

    override fun findAvailableSeats(concertScheduleId: Long): List<ConcertSeat> {
        return concertSeatJpaRepository.findAvailableSeats(concertScheduleId).map {
            it.toModel()
        }
    }

    override fun getById(id: Long): ConcertSeat {
        return concertSeatJpaRepository.findByIdOrNull(id)?.toModel()
            ?: throw IllegalStateException("존재하지 않는 콘서트 좌석입니다.")
    }
}