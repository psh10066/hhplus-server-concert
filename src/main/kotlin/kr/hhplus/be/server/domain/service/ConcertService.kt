package kr.hhplus.be.server.domain.service

import kr.hhplus.be.server.domain.model.common.dto.PageDto
import kr.hhplus.be.server.domain.model.concert.*
import kr.hhplus.be.server.support.error.CustomException
import kr.hhplus.be.server.support.error.ErrorType
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ConcertService(
    private val concertRepository: ConcertRepository,
    private val concertScheduleRepository: ConcertScheduleRepository,
    private val concertSeatRepository: ConcertSeatRepository,
) {
    private val log: Logger = LoggerFactory.getLogger(javaClass)

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

    @Transactional
    fun cancelSeatReservation(concertSeatIds: List<Long>) {
        val concertSeats = concertSeatRepository.getAllById(concertSeatIds)
        concertSeats.forEach { concertSeat ->
            concertSeat.cancelReservation()
            try {
                concertSeatRepository.saveAndFlush(concertSeat)
            } catch (e: OptimisticLockingFailureException) {
                // 좌석 예약 만료 시간 직전 결제 요청, 만료 직후 예약 취소가 동시에 동작하는 경우 등
                // 극히 드물게 발생할 수 있으나 발생하더라도 동시성 처리가 잘 된 경우로, 로그만 기록한다.
                log.info("[cancelSeatReservation] ID=[${concertSeat.id}] 좌석 예약 취소 실패")
            }
        }
    }
}