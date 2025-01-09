package kr.hhplus.be.server.api.controller.v1

import kr.hhplus.be.server.api.controller.v1.request.ConcertReservationRequest
import kr.hhplus.be.server.api.controller.v1.response.ConcertReservationResponse
import kr.hhplus.be.server.domain.model.queue.dto.QueueInfo
import kr.hhplus.be.server.support.response.ApiResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/reservations")
class ReservationController {

    @PostMapping("/concert")
    fun concertReservation(
        @RequestBody request: ConcertReservationRequest,
        queueInfo: QueueInfo
    ): ApiResponse<ConcertReservationResponse> {
        return ApiResponse.success(ConcertReservationResponse(reservationId = 1))
    }
}