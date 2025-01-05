package kr.hhplus.be.server.api.controller.v1

import kr.hhplus.be.server.api.controller.v1.request.ConcertPaymentRequest
import kr.hhplus.be.server.api.controller.v1.response.ConcertPaymentResponse
import kr.hhplus.be.server.support.response.ApiResponse
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/payment")
class PaymentController {

    @PostMapping("/concert")
    fun concertPayment(
        @RequestBody request: ConcertPaymentRequest,
        @RequestHeader token: String
    ): ApiResponse<ConcertPaymentResponse> {
        return ApiResponse.success(ConcertPaymentResponse(paymentHistoryId = 1L))
    }
}