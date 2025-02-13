package kr.hhplus.be.server.domain.event

import kr.hhplus.be.server.domain.model.reservation.Reservation

data class ConcertReservationExpiredEvent(
    val reservations: List<Reservation>
)