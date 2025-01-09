package kr.hhplus.be.server.domain.model.reservation

interface ReservationRepository {

    fun findConcertReservation(concertScheduleId: Long, concertSeatId: Long): List<Reservation>

    fun save(reservation: Reservation): Reservation

}