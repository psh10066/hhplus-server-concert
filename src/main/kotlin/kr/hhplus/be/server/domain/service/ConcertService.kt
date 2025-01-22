package kr.hhplus.be.server.domain.service

import kr.hhplus.be.server.domain.model.common.dto.PageDto
import kr.hhplus.be.server.domain.model.concert.*
import kr.hhplus.be.server.support.error.CustomException
import kr.hhplus.be.server.support.error.ErrorType
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

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

    @Transactional
    fun reserveSeat(concertSeatId: Long) {
        val concertSeat = concertSeatRepository.getById(concertSeatId)
        concertSeat.reserve()
        try {
            concertSeatRepository.saveAndFlush(concertSeat)
        } catch (e: OptimisticLockingFailureException) {
            throw CustomException(ErrorType.ALREADY_RESERVED_CONCERT_SEAT)
        }
    }

    @Transactional
    fun completePaymentSeat(concertSeatId: Long) {
        val concertSeat = concertSeatRepository.getById(concertSeatId)
        concertSeat.completePayment()
        try {
            concertSeatRepository.saveAndFlush(concertSeat)
        } catch (e: OptimisticLockingFailureException) {
            throw CustomException(ErrorType.NOT_PAYABLE_RESERVATION)
        }
    }
}