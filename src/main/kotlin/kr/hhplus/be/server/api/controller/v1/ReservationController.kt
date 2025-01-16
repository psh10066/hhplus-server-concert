package kr.hhplus.be.server.api.controller.v1

import kr.hhplus.be.server.api.controller.v1.request.ConcertPaymentRequest
import kr.hhplus.be.server.api.controller.v1.request.ConcertReservationRequest
import kr.hhplus.be.server.api.controller.v1.response.ConcertPaymentResponse
import kr.hhplus.be.server.api.controller.v1.response.ConcertReservationResponse
import kr.hhplus.be.server.application.ReservationFacade
import kr.hhplus.be.server.domain.model.queue.Queue
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
        queue: Queue
    ): ApiResponse<ConcertReservationResponse> {
        val reservationId = reservationFacade.concertReservation(
            queue = queue,
            concertScheduleId = request.concertScheduleId,
            concertSeatId = request.concertSeatId
        )
        return ApiResponse.success(ConcertReservationResponse(reservationId = reservationId))
    }

    @PostMapping("/concert/payment")
    fun concertPayment(
        @RequestBody request: ConcertPaymentRequest,
        queue: Queue
    ): ApiResponse<ConcertPaymentResponse> {
        val paymentHistoryId = reservationFacade.concertPayment(
            queue = queue,
            reservationId = request.reservationId
        )
        return ApiResponse.success(ConcertPaymentResponse(paymentHistoryId = paymentHistoryId))
    }
}