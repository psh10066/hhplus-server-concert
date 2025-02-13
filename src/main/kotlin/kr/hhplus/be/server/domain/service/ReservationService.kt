package kr.hhplus.be.server.domain.service

import kr.hhplus.be.server.domain.event.ConcertReservationFinishedEvent
import kr.hhplus.be.server.domain.model.reservation.ConcertReservationCount
import kr.hhplus.be.server.domain.model.reservation.Reservation
import kr.hhplus.be.server.domain.model.reservation.ReservationRepository
import kr.hhplus.be.server.domain.model.user.User
import kr.hhplus.be.server.support.client.ConcertApiClient
import kr.hhplus.be.server.support.error.CustomException
import kr.hhplus.be.server.support.error.ErrorType
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Clock
import java.time.LocalDate

@Service
class ReservationService(
    private val clock: Clock,
    private val reservationRepository: ReservationRepository,
    private val concertApiClient: ConcertApiClient,
    private val applicationEventPublisher: ApplicationEventPublisher
) {

    @Transactional
    fun concertReservation(user: User, concertSeatId: Long): Long {
        concertApiClient.reserveSeat(concertSeatId)
        try {
            val concert = concertApiClient.getConcertBySeatId(concertSeatId)
            val reservations = reservationRepository.findConcertReservation(concertSeatId)
            if (reservations.any { it.isReserved(clock) }) {
                throw CustomException(ErrorType.ALREADY_RESERVED_CONCERT_SEAT)
            }

            val reservation = Reservation.reserve(clock, concert.id, concertSeatId, user.id)
            val savedReservation = reservationRepository.save(reservation)

            return savedReservation.id
        } finally {
            applicationEventPublisher.publishEvent(ConcertReservationFinishedEvent(user, concertSeatId))
        }
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