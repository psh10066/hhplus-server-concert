package kr.hhplus.be.server.domain.service

import kr.hhplus.be.server.domain.model.reservation.Reservation
import kr.hhplus.be.server.domain.model.reservation.ReservationRepository
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
            throw IllegalStateException("이미 예약된 좌석입니다.")
        }

        val reservation = Reservation.book(clock, concertScheduleId, concertSeatId, userId)
        reservationRepository.save(reservation)
        return reservation.id
    }

    fun payReservation(id: Long): Reservation {
        val reservation = reservationRepository.getById(id)
        if (!reservation.isPayable(clock)) {
            throw IllegalStateException("결제 가능한 예약이 아닙니다.")
        }
        reservation.pay()
        reservationRepository.save(reservation)
        return reservation
    }
}