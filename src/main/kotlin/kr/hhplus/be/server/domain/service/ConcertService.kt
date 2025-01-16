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

    fun getConcertBySeatId(concertSeatId: Long): Concert {
        val concertSeat = concertSeatRepository.getById(concertSeatId)
        val concertSchedule = concertScheduleRepository.getById(concertSeat.concertScheduleId)
        return concertRepository.getById(concertSchedule.concertId)
    }

    fun getConcertSeat(concertSeatId: Long): ConcertSeat {
        return concertSeatRepository.getById(concertSeatId)
    }
}