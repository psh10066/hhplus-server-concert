package kr.hhplus.be.server.api.controller.v1

import kr.hhplus.be.server.api.controller.v1.request.ConcertPaymentRequest
import kr.hhplus.be.server.api.controller.v1.request.ConcertReservationRequest
import kr.hhplus.be.server.api.controller.v1.response.ConcertPaymentResponse
import kr.hhplus.be.server.api.controller.v1.response.ConcertReservationResponse
import kr.hhplus.be.server.application.ReservationFacade
import kr.hhplus.be.server.domain.model.queue.dto.QueueInfo
import kr.hhplus.be.server.support.response.ApiResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/reservations")
class ReservationController(
    private val reservationFacade: ReservationFacade
) {

    @PostMapping("/concert")
    fun concertReservation(
        @RequestBody request: ConcertReservationRequest,
        queueInfo: QueueInfo
    ): ApiResponse<ConcertReservationResponse> {
        val reservationId = reservationFacade.concertReservation(
            queueInfo = queueInfo,
            concertScheduleId = request.concertScheduleId,
            concertSeatId = request.concertSeatId
        )
        return ApiResponse.success(ConcertReservationResponse(reservationId = reservationId))
    }

    @PostMapping("/concert/payment")
    fun concertPayment(
        @RequestBody request: ConcertPaymentRequest,
        queueInfo: QueueInfo
    ): ApiResponse<ConcertPaymentResponse> {
        val paymentHistoryId = reservationFacade.concertPayment(
            queueInfo = queueInfo,
            reservationId = request.reservationId
        )
        return ApiResponse.success(ConcertPaymentResponse(paymentHistoryId = paymentHistoryId))
    }
}