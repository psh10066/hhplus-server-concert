package kr.hhplus.be.server.infrastructure.dao.reservation

import kr.hhplus.be.server.domain.model.reservation.ConcertReservationCount
import kr.hhplus.be.server.domain.model.reservation.Reservation
import kr.hhplus.be.server.domain.model.reservation.ReservationRepository
import kr.hhplus.be.server.support.error.CustomException
import kr.hhplus.be.server.support.error.ErrorType
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import java.time.LocalDate

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

    override fun findAll(): List<Reservation> {
        return reservationJpaRepository.findAll().map {
            it.toModel()
        }
    }

    override fun deleteAll(reservations: List<Reservation>) {
        return reservationJpaRepository.deleteAll(reservations.map {
            ReservationEntity.from(it)
        })
    }

    override fun findConcertReservationCountsByDate(date: LocalDate, size: Int): List<ConcertReservationCount> {
        return reservationJpaRepository.findConcertReservationCountsByDate(date, size)
    }
}