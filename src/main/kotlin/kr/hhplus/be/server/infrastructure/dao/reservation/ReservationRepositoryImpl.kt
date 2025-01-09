package kr.hhplus.be.server.infrastructure.dao.reservation

import kr.hhplus.be.server.domain.model.reservation.Reservation
import kr.hhplus.be.server.domain.model.reservation.ReservationRepository
import org.springframework.stereotype.Component

@Component
class ReservationRepositoryImpl(
    private val reservationJpaRepository: ReservationJpaRepository
) : ReservationRepository {

    override fun findConcertReservation(concertScheduleId: Long, concertSeatId: Long): List<Reservation> {
        return reservationJpaRepository.findByConcertScheduleIdAndConcertSeatId(concertScheduleId, concertSeatId)
    }

    override fun save(reservation: Reservation): Reservation {
        return reservationJpaRepository.save(reservation)
    }
}