package kr.hhplus.be.server.infrastructure.dao.reservation

import kr.hhplus.be.server.domain.model.reservation.Reservation
import kr.hhplus.be.server.domain.model.reservation.ReservationRepository
import kr.hhplus.be.server.support.error.CustomException
import kr.hhplus.be.server.support.error.ErrorType
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class ReservationRepositoryImpl(
    private val reservationJpaRepository: ReservationJpaRepository
) : ReservationRepository {

    override fun findConcertReservation(concertSeatId: Long): List<Reservation> {
        return reservationJpaRepository.findByConcertSeatId(concertSeatId).map {
            it.toModel()
        }
    }

    override fun save(reservation: Reservation): Reservation {
        return reservationJpaRepository.save(ReservationEntity.from(reservation)).toModel()
    }

    override fun getById(id: Long): Reservation {
        return reservationJpaRepository.findByIdOrNull(id)?.toModel()
            ?: throw CustomException(ErrorType.RESERVATION_NOT_FOUND)
    }
}