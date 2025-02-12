package kr.hhplus.be.server.domain.service

import kr.hhplus.be.server.domain.model.reservation.ConcertReservationCount
import kr.hhplus.be.server.domain.model.reservation.Reservation
import kr.hhplus.be.server.domain.model.reservation.ReservationRepository
import kr.hhplus.be.server.support.error.CustomException
import kr.hhplus.be.server.support.error.ErrorType
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Clock
import java.time.LocalDate

@Service
class ReservationService(
    private val clock: Clock,
    private val reservationRepository: ReservationRepository
) {

    @Transactional
    fun concertReservation(userId: Long, concertSeatId: Long): Long {
        val reservations = reservationRepository.findConcertReservation(concertSeatId)
        if (reservations.any { it.isReserved(clock) }) {
            throw CustomException(ErrorType.ALREADY_RESERVED_CONCERT_SEAT)
        }

        val reservation = Reservation.reserve(clock, concertSeatId, userId)
        return reservationRepository.save(reservation).id
    }

    fun payReservation(id: Long): Reservation {
        val reservation = reservationRepository.getById(id)
        reservation.pay(clock)
        return reservationRepository.save(reservation)
    }

    fun expireReservations(): List<Reservation> {
        val reservations = reservationRepository.findAll().filter { !it.isReserved(clock) }
        reservationRepository.deleteAll(reservations)
        return reservations
    }

    fun getConcertReservationCounts(date: LocalDate, size: Int): List<ConcertReservationCount> {
        return reservationRepository.findConcertReservationCountsByDate(date, size)
    }
}