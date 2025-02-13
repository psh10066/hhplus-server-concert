package kr.hhplus.be.server.application

import kr.hhplus.be.server.domain.model.concert.Concert
import kr.hhplus.be.server.domain.model.user.User
import kr.hhplus.be.server.domain.service.*
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.Clock
import java.time.LocalDate

@Component
class ReservationFacade(
    private val clock: Clock,
    private val userService: UserService,
    private val queueService: QueueService,
    private val concertService: ConcertService,
    private val reservationService: ReservationService,
    private val paymentService: PaymentService
) {

    @Transactional
    fun concertReservation(user: User, concertSeatId: Long): Long {
        concertService.reserveSeat(concertSeatId)
        val concert = concertService.getConcertBySeatId(concertSeatId)
        val reservationId = reservationService.concertReservation(
            userId = user.id,
            concertId = concert.id,
            concertSeatId = concertSeatId
        )
        queueService.readyPayment(user.uuid)
        return reservationId
    }

    @Transactional
    fun expireReservations() {
        val reservations = reservationService.expireReservations()
        concertService.cancelSeatReservation(reservations.map { it.concertSeatId })
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

    @Cacheable(value = ["popularConcerts"])
    @Transactional(readOnly = true)
    fun getPopularConcerts(): List<Concert> {
        val size = 20
        val reservationCounts = reservationService.getConcertReservationCounts(LocalDate.now(clock), size)
        val concerts = concertService.findConcerts(reservationCounts.map { it.concertId })

        val concertMap: Map<Long, Concert> = concerts.associateBy { it.id }
        return reservationCounts
            .sortedByDescending { it.count }
            .mapNotNull { concertMap[it.concertId] }
    }
}