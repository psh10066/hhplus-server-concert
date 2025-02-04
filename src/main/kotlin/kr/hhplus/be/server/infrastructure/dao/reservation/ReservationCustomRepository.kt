package kr.hhplus.be.server.infrastructure.dao.reservation

import kr.hhplus.be.server.domain.model.reservation.ConcertReservationCount
import java.time.LocalDate

interface ReservationCustomRepository {

    fun findConcertReservationCountsByDate(date: LocalDate): List<ConcertReservationCount>
}