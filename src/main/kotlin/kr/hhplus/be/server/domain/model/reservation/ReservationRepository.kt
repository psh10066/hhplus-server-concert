package kr.hhplus.be.server.domain.model.reservation

import java.time.LocalDate

interface ReservationRepository {

    fun findConcertReservation(concertSeatId: Long): List<Reservation>

    fun save(reservation: Reservation): Reservation

    fun getById(id: Long): Reservation

    fun findAll(): List<Reservation>

    fun deleteAll(reservations: List<Reservation>)

    fun findConcertReservationCountsByDate(date: LocalDate): List<ConcertReservationCount>
}