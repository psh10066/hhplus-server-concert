package kr.hhplus.be.server.domain.service

import kr.hhplus.be.server.domain.event.ConcertReservationExpiredEvent
import kr.hhplus.be.server.domain.event.ConcertReservationFinishedEvent
import kr.hhplus.be.server.domain.model.concert.Concert
import kr.hhplus.be.server.domain.model.reservation.Reservation
import kr.hhplus.be.server.domain.model.reservation.ReservationRepository
import kr.hhplus.be.server.domain.model.user.User
import kr.hhplus.be.server.support.client.ConcertApiClient
import kr.hhplus.be.server.support.error.CustomException
import kr.hhplus.be.server.support.error.ErrorType
import org.springframework.cache.annotation.Cacheable
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Clock
import java.time.LocalDate
import java.time.LocalDateTime

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

    fun getReservation(id: Long): Reservation {
        return reservationRepository.getById(id)
    }

    fun payReservation(id: Long): Reservation {
        val reservation = reservationRepository.getById(id)
        reservation.pay(clock)
        return reservationRepository.save(reservation)
    }

    fun rollbackPayReservation(id: Long, expiredAt: LocalDateTime): Reservation {
        val reservation = reservationRepository.getById(id)
        reservation.rollbackPay(expiredAt)
        return reservationRepository.save(reservation)
    }

    @Transactional
    fun expireReservations(): List<Reservation> {
        val reservations = reservationRepository.findAll().filter { !it.isReserved(clock) }
        reservationRepository.deleteAll(reservations)

        applicationEventPublisher.publishEvent(ConcertReservationExpiredEvent(reservations))
        return reservations
    }

    @Cacheable(value = ["popularConcerts"])
    fun getPopularConcerts(): List<Concert> {
        val size = 20
        val reservationCounts = reservationRepository.findConcertReservationCountsByDate(LocalDate.now(clock), size)
        val concerts = concertApiClient.findConcerts(reservationCounts.map { it.concertId })

        val concertMap: Map<Long, Concert> = concerts.associateBy { it.id }
        return reservationCounts
            .sortedByDescending { it.count }
            .mapNotNull { concertMap[it.concertId] }
    }
}