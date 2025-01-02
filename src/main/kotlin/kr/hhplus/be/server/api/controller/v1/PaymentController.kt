package kr.hhplus.be.server.api.controller.v1

import kr.hhplus.be.server.api.controller.v1.response.ConcertPaymentResponse
import kr.hhplus.be.server.support.response.ApiResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/payment")
class PaymentController {

    @PostMapping("/concert")
    fun concertPayment(
        @RequestHeader token: String
    ): ApiResponse<ConcertPaymentResponse> {
        return ApiResponse.success(ConcertPaymentResponse(paymentHistoryId = 1L))
    }
}