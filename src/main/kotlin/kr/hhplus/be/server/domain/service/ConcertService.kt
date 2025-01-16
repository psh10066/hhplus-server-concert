package kr.hhplus.be.server.domain.service

import kr.hhplus.be.server.domain.model.common.dto.PageDto
import kr.hhplus.be.server.domain.model.concert.*
import org.springframework.stereotype.Service

@Service
class ConcertService(
    private val concertRepository: ConcertRepository,
    private val concertScheduleRepository: ConcertScheduleRepository,
    private val concertSeatRepository: ConcertSeatRepository,
) {

    fun findPage(page: Int, size: Int): PageDto<Concert> {
        return concertRepository.findPage(page, size)
    }

    fun findConcertSchedules(concertId: Long): List<ConcertSchedule> {
        return concertScheduleRepository.findAvailablesByConcertId(concertId)
    }

    fun findAvailableSeats(concertScheduleId: Long): List<ConcertSeat> {
        return concertSeatRepository.findAvailableSeats(concertScheduleId)
    }

    fun getConcertByScheduleId(concertScheduleId: Long): Concert {
        val concertSchedule = concertScheduleRepository.getById(concertScheduleId)
        return concertRepository.getById(concertSchedule.concertId)
    }

    fun getConcertSchedule(concertScheduleId: Long): ConcertSchedule {
        return concertScheduleRepository.getById(concertScheduleId)
    }

    fun getConcertSeat(concertSeatId: Long): ConcertSeat {
        return concertSeatRepository.getById(concertSeatId)
    }
}