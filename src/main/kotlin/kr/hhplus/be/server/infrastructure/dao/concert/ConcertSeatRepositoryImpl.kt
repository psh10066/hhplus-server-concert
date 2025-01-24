package kr.hhplus.be.server.infrastructure.dao.concert

import kr.hhplus.be.server.domain.model.concert.ConcertSeat
import kr.hhplus.be.server.domain.model.concert.ConcertSeatRepository
import kr.hhplus.be.server.support.error.CustomException
import kr.hhplus.be.server.support.error.ErrorType
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
            ?: throw CustomException(ErrorType.CONCERT_SEAT_NOT_FOUND)
    }

    override fun saveAndFlush(concertSeat: ConcertSeat): ConcertSeat {
        return concertSeatJpaRepository.saveAndFlush(ConcertSeatEntity.from(concertSeat)).toModel()
    }

    override fun getAllById(concertSeatIds: List<Long>): List<ConcertSeat> {
        return concertSeatJpaRepository.findAllById(concertSeatIds).map {
            it.toModel()
        }
    }
}