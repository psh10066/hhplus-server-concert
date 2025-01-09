package kr.hhplus.be.server.application

import kr.hhplus.be.server.domain.service.ConcertService
import kr.hhplus.be.server.domain.service.ReservationService
import kr.hhplus.be.server.domain.service.UserService
import org.springframework.stereotype.Component
import java.util.*

@Component
class ReservationFacade(
    private val userService: UserService,
    private val concertService: ConcertService,
    private val reservationService: ReservationService
) {

    fun concertReservation(userUuid: UUID, concertScheduleId: Long, concertSeatId: Long): Long {
        val user = userService.getUserInfo(userUuid)
        val concertSchedule = concertService.getConcertSchedule(concertScheduleId)
        val concertSeat = concertService.getConcertSeat(concertSeatId)
        val reservationId = reservationService.concertReservation(
            userId = user.id,
            concertScheduleId = concertSchedule.id,
            concertSeatId = concertSeat.id
        )
        return reservationId
    }
}