package kr.hhplus.be.server.api.controller.v1

import kr.hhplus.be.server.api.controller.v1.request.ChargeBalanceRequest
import kr.hhplus.be.server.api.controller.v1.response.UserWalletBalanceResponse
import kr.hhplus.be.server.domain.model.user.dto.UserInfo
import kr.hhplus.be.server.support.response.ApiResponse
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/user-wallets")
class UserWalletController {

    @GetMapping("/balance")
    fun getBalance(
        userInfo: UserInfo
    ): ApiResponse<UserWalletBalanceResponse> {
        return ApiResponse.success(UserWalletBalanceResponse(userId = userInfo.id, balance = 1000))
    }

    @PatchMapping("/balance")
    fun chargeBalance(
        @RequestBody request: ChargeBalanceRequest,
        userInfo: UserInfo
    ): ApiResponse<Any> {
        return ApiResponse.success()
    }
}