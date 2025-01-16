package kr.hhplus.be.server.domain.model.reservation

interface ReservationRepository {

    fun findConcertReservation(concertSeatId: Long): List<Reservation>

    fun save(reservation: Reservation): Reservation

    fun getById(id: Long): Reservation
}