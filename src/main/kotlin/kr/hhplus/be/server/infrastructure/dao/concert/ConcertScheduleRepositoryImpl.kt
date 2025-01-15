package kr.hhplus.be.server.infrastructure.dao.concert

import kr.hhplus.be.server.domain.model.concert.ConcertSchedule
import kr.hhplus.be.server.domain.model.concert.ConcertScheduleRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import java.time.Clock
import java.time.LocalDateTime

@Component
class ConcertScheduleRepositoryImpl(
    private val clock: Clock,
    private val concertScheduleJpaRepository: ConcertScheduleJpaRepository
) : ConcertScheduleRepository {

    override fun findAvailablesByConcertId(concertId: Long): List<ConcertSchedule> {
        return concertScheduleJpaRepository.findByConcertIdAndStartTimeAfter(concertId, LocalDateTime.now(clock)).map {
            it.toModel()
        }
    }

    override fun getById(id: Long): ConcertSchedule {
        return concertScheduleJpaRepository.findByIdOrNull(id)?.toModel()
            ?: throw IllegalStateException("존재하지 않는 콘서트 날짜입니다.")
    }
}