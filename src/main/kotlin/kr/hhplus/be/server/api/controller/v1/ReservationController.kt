package kr.hhplus.be.server.api.controller.v1

import kr.hhplus.be.server.api.controller.v1.request.ConcertPaymentRequest
import kr.hhplus.be.server.api.controller.v1.request.ConcertReservationRequest
import kr.hhplus.be.server.api.controller.v1.response.ConcertPaymentResponse
import kr.hhplus.be.server.api.controller.v1.response.ConcertReservationResponse
import kr.hhplus.be.server.api.controller.v1.response.GetPopularConcertResponse
import kr.hhplus.be.server.application.ReservationFacade
import kr.hhplus.be.server.domain.model.user.User
import kr.hhplus.be.server.domain.service.ReservationService
import kr.hhplus.be.server.support.response.ApiResponse
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/reservations")
class ReservationController(
    private val reservationFacade: ReservationFacade,
    private val reservationService: ReservationService
) {

    @PostMapping("/concert")
    fun concertReservation(
        @RequestBody request: ConcertReservationRequest,
        user: User
    ): ApiResponse<ConcertReservationResponse> {
        val reservationId = reservationService.concertReservation(
            user = user,
            concertSeatId = request.concertSeatId
        )
        return ApiResponse.success(ConcertReservationResponse(reservationId = reservationId))
    }

    @PostMapping("/concert/payment")
    fun concertPayment(
        @RequestBody request: ConcertPaymentRequest,
        user: User
    ): ApiResponse<ConcertPaymentResponse> {
        val paymentHistoryId = reservationFacade.concertPayment(
            user = user,
            reservationId = request.reservationId
        )
        return ApiResponse.success(ConcertPaymentResponse(paymentHistoryId = paymentHistoryId))
    }

    @GetMapping("/concert/popular")
    fun popularConcerts(
        user: User
    ): ApiResponse<GetPopularConcertResponse> {
        val popularConcerts = reservationFacade.getPopularConcerts()
        val response = GetPopularConcertResponse(popularConcerts.map {
            GetPopularConcertResponse.ConcertDto.of(it)
        })
        return ApiResponse.success(response)
    }
}