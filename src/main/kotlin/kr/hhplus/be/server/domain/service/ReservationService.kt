package kr.hhplus.be.server.domain.service

import kr.hhplus.be.server.domain.model.reservation.Reservation
import kr.hhplus.be.server.domain.model.reservation.ReservationRepository
import kr.hhplus.be.server.support.error.CustomException
import kr.hhplus.be.server.support.error.ErrorType
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Clock

@Service
class ReservationService(
    private val clock: Clock,
    private val reservationRepository: ReservationRepository
) {

    @Transactional
    fun concertReservation(userId: Long, concertScheduleId: Long, concertSeatId: Long): Long {
        val reservations = reservationRepository.findConcertReservation(concertScheduleId, concertSeatId)
        if (reservations.any { it.isBooked(clock) }) {
            throw CustomException(ErrorType.ALREADY_BOOKED_CONCERT_SEAT)
        }

        val reservation = Reservation.book(clock, concertScheduleId, concertSeatId, userId)
        reservationRepository.save(reservation)
        return reservation.id
    }

    fun payReservation(id: Long): Reservation {
        val reservation = reservationRepository.getById(id)
        reservation.pay(clock)
        reservationRepository.save(reservation)
        return reservation
    }
}