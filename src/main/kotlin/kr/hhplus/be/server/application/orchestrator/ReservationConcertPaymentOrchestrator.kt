package kr.hhplus.be.server.application.orchestrator

import kr.hhplus.be.server.domain.service.ReservationService
import kr.hhplus.be.server.support.client.ConcertApiClient
import kr.hhplus.be.server.support.client.PaymentApiClient
import kr.hhplus.be.server.support.client.UserApiClient
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*

enum class ReservationConcertPaymentFlow {
    PAY_RESERVATION,
    COMPLETE_PAYMENT_SEAT,
    USE_BALANCE,
    PAY
}

@Component
class ReservationConcertPaymentOrchestrator(
    private val reservationService: ReservationService,
    private val concertApiClient: ConcertApiClient,
    private val userApiClient: UserApiClient,
    private val paymentApiClient: PaymentApiClient,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    private val flowSet = ThreadLocal<EnumSet<ReservationConcertPaymentFlow>>()
    private val outbox = ThreadLocal<OutBox>()

    private data class OutBox(
        val userId: Long,
        val reservationId: Long,
        var reservationExpiredAt: LocalDateTime? = null,
        var concertSeatId: Long = 0L,
        var amount: Long = 0L,
        var paymentHistoryId: Long = 0L,
    )

    fun init(userId: Long, reservationId: Long) {
        flowSet.set(EnumSet.noneOf(ReservationConcertPaymentFlow::class.java))
        outbox.set(OutBox(userId = userId, reservationId = reservationId))
    }

    fun executed(flow: ReservationConcertPaymentFlow, id: Long, optionalData: Any? = null) {
        flowSet.get().add(flow)

        val data = outbox.get()
        when (flow) {
            ReservationConcertPaymentFlow.PAY_RESERVATION -> data.reservationExpiredAt = optionalData as LocalDateTime
            ReservationConcertPaymentFlow.COMPLETE_PAYMENT_SEAT -> data.concertSeatId = id
            ReservationConcertPaymentFlow.USE_BALANCE -> data.amount = optionalData as Long
            ReservationConcertPaymentFlow.PAY -> data.paymentHistoryId = id
        }
    }

    private fun rollbackFlow(currentFlow: ReservationConcertPaymentFlow) {
        val data = outbox.get()
        when (currentFlow) {
            ReservationConcertPaymentFlow.PAY_RESERVATION -> reservationService.rollbackPayReservation(data.reservationId, data.reservationExpiredAt!!)
            ReservationConcertPaymentFlow.COMPLETE_PAYMENT_SEAT -> concertApiClient.rollbackPaymentSeat(data.concertSeatId)
            ReservationConcertPaymentFlow.USE_BALANCE -> userApiClient.chargeBalance(data.userId, data.amount)
            ReservationConcertPaymentFlow.PAY -> paymentApiClient.rollbackPay(data.paymentHistoryId)
        }
    }

    fun rollbackExecuted() {
        flowSet.get().forEach {
            try {
                rollbackFlow(it)
            } catch (e: Exception) {
                log.error("[ReservationConcertPaymentOrchestrator] 롤백 실패 : $it", e)
            }
        }
    }

    fun clear() {
        flowSet.remove()
        outbox.remove()
    }
}