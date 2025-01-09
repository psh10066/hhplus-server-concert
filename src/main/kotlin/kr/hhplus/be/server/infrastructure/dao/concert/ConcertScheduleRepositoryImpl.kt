package kr.hhplus.be.server.infrastructure.dao.concert

import kr.hhplus.be.server.domain.model.concert.ConcertSchedule
import kr.hhplus.be.server.domain.model.concert.ConcertScheduleRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class ConcertScheduleRepositoryImpl(
    private val concertScheduleJpaRepository: ConcertScheduleJpaRepository
) : ConcertScheduleRepository {

    override fun findByConcertId(concertId: Long): List<ConcertSchedule> {
        return concertScheduleJpaRepository.findByConcertId(concertId)
    }

    override fun getById(id: Long): ConcertSchedule {
        return concertScheduleJpaRepository.findByIdOrNull(id)
            ?: throw IllegalStateException("존재하지 않는 콘서트 날짜입니다.")
    }
}