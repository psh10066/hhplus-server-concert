package kr.hhplus.be.server.application

import kr.hhplus.be.server.application.orchestrator.ReservationConcertPaymentFlow
import kr.hhplus.be.server.application.orchestrator.ReservationConcertPaymentOrchestrator
import kr.hhplus.be.server.domain.event.ReservationConcertPaymentSucceedEvent
import kr.hhplus.be.server.domain.model.concert.Concert
import kr.hhplus.be.server.domain.model.user.User
import kr.hhplus.be.server.domain.service.ConcertService
import kr.hhplus.be.server.domain.service.ReservationService
import kr.hhplus.be.server.support.client.ConcertApiClient
import kr.hhplus.be.server.support.client.PaymentApiClient
import kr.hhplus.be.server.support.client.UserApiClient
import org.springframework.cache.annotation.Cacheable
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.Clock
import java.time.LocalDate

@Component
class ReservationFacade(
    private val clock: Clock,
    private val concertService: ConcertService,
    private val reservationService: ReservationService,
    private val concertApiClient: ConcertApiClient,
    private val userApiClient: UserApiClient,
    private val paymentApiClient: PaymentApiClient,
    private val orchestrator: ReservationConcertPaymentOrchestrator,
    private val applicationEventPublisher: ApplicationEventPublisher
) {

    fun concertPayment(user: User, reservationId: Long): Long {
        val reservation = reservationService.getReservation(reservationId)

        orchestrator.init(userId = user.id, reservationId = reservationId)
        try {
            reservationService.payReservation(reservationId)
            orchestrator.executed(ReservationConcertPaymentFlow.PAY_RESERVATION, reservation.id, reservation.expiredAt)

            concertApiClient.completePaymentSeat(reservation.concertSeatId)
            orchestrator.executed(ReservationConcertPaymentFlow.COMPLETE_PAYMENT_SEAT, reservation.concertSeatId)

            val concert = concertApiClient.getConcertBySeatId(reservation.concertSeatId)

            userApiClient.useBalance(userId = user.id, amount = concert.price)
            orchestrator.executed(ReservationConcertPaymentFlow.USE_BALANCE, user.id, concert.price)

            val paymentHistory = paymentApiClient.pay(reservationId = reservation.id, userId = user.id, amount = concert.price)
            orchestrator.executed(ReservationConcertPaymentFlow.PAY, paymentHistory.id)

            applicationEventPublisher.publishEvent(ReservationConcertPaymentSucceedEvent(user))
            return paymentHistory.id

        } catch (e: Exception) {
            orchestrator.rollbackExecuted()
            throw e

        } finally {
            orchestrator.clear()
        }
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