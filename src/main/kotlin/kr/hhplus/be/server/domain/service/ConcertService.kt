package kr.hhplus.be.server.domain.service

import kr.hhplus.be.server.domain.model.common.dto.PageDto
import kr.hhplus.be.server.domain.model.concert.ConcertRepository
import kr.hhplus.be.server.domain.model.concert.ConcertScheduleRepository
import kr.hhplus.be.server.domain.model.concert.dto.ConcertInfo
import kr.hhplus.be.server.domain.model.concert.dto.ConcertScheduleInfo
import org.springframework.stereotype.Service

@Service
class ConcertService(
    private val concertRepository: ConcertRepository,
    private val concertScheduleRepository: ConcertScheduleRepository,
) {

    fun findPage(page: Int, size: Int): PageDto<ConcertInfo> {
        return concertRepository.findPage(page, size).map { ConcertInfo.of(it) }
    }

    fun findConcertSchedules(concertId: Long): List<ConcertScheduleInfo> {
        return concertScheduleRepository.findByConcertId(concertId).map { ConcertScheduleInfo.of(it) }
    }
}