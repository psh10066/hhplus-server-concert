package kr.hhplus.be.server.application

import kr.hhplus.be.server.domain.model.user.User
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

    @Transactional
    fun concertReservation(user: User, concertSeatId: Long): Long {
        concertService.reserveSeat(concertSeatId)
        val reservationId = reservationService.concertReservation(
            userId = user.id,
            concertSeatId = concertSeatId
        )
        queueService.readyPayment(user.uuid)
        return reservationId
    }

    @Transactional
    fun concertPayment(user: User, reservationId: Long): Long {
        val reservation = reservationService.payReservation(reservationId)
        concertService.completePaymentSeat(reservation.concertSeatId)

        val concert = concertService.getConcertBySeatId(reservation.concertSeatId)

        userService.useBalance(userId = user.id, amount = concert.price)
        val paymentHistory = paymentService.pay(reservationId = reservation.id, userId = user.id, amount = concert.price)

        queueService.expire(user.uuid)
        return paymentHistory.id
    }
}