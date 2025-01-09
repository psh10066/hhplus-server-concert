package kr.hhplus.be.server.application

import kr.hhplus.be.server.domain.model.queue.dto.QueueInfo
import kr.hhplus.be.server.domain.service.*
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class ReservationFacade(
    private val userService: UserService,
    private val queueService: QueueService,
    private val concertService: ConcertService,
    private val reservationService: ReservationService,
    private val paymentService: PaymentService
) {

    fun concertReservation(queueInfo: QueueInfo, concertScheduleId: Long, concertSeatId: Long): Long {
        val user = userService.getUserInfo(queueInfo.userUuid)
        val concertSchedule = concertService.getConcertSchedule(concertScheduleId)
        val concertSeat = concertService.getConcertSeat(concertSeatId)
        val reservationId = reservationService.concertReservation(
            userId = user.id,
            concertScheduleId = concertSchedule.id,
            concertSeatId = concertSeat.id
        )
        queueService.readyPayment(queueInfo.token)
        return reservationId
    }

    @Transactional
    fun concertPayment(queueInfo: QueueInfo, reservationId: Long): Long {
        val user = userService.getUserInfo(queueInfo.userUuid)
        val reservation = reservationService.payReservation(reservationId)
        val concert = concertService.getConcertByScheduleId(reservation.concertScheduleId)

        userService.useBalance(userId = user.id, amount = concert.price)
        val paymentHistory = paymentService.pay(reservationId = reservation.id, userId = user.id, amount = concert.price)

        queueService.expire(queueInfo.id)
        return paymentHistory.id
    }
}