package kr.hhplus.be.server.api.controller.v1

import kr.hhplus.be.server.api.controller.v1.request.ConcertReservationRequest
import kr.hhplus.be.server.api.controller.v1.response.ConcertReservationResponse
import kr.hhplus.be.server.support.response.ApiResponse
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/reservations")
class ReservationController {

    @PostMapping("/concert")
    fun concertReservation(
        @RequestBody request: ConcertReservationRequest,
        @RequestHeader token: String
    ): ApiResponse<ConcertReservationResponse> {
        return ApiResponse.success(ConcertReservationResponse(reservationId = 1))
    }
}