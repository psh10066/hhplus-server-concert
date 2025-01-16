package kr.hhplus.be.server.api.controller.v1

import kr.hhplus.be.server.api.controller.v1.request.ChargeBalanceRequest
import kr.hhplus.be.server.api.controller.v1.response.UserWalletBalanceResponse
import kr.hhplus.be.server.domain.model.user.User
import kr.hhplus.be.server.domain.service.UserService
import kr.hhplus.be.server.support.response.ApiResponse
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/user-wallets")
class UserWalletController(
    private val userService: UserService
) {

    @GetMapping("/balance")
    fun getBalance(
        user: User
    ): ApiResponse<UserWalletBalanceResponse> {
        val balance = userService.getBalanceByUserId(user.id)
        return ApiResponse.success(UserWalletBalanceResponse(userId = user.id, balance = balance))
    }

    @PatchMapping("/balance")
    fun chargeBalance(
        @RequestBody request: ChargeBalanceRequest,
        user: User
    ): ApiResponse<Any> {
        val amount = request.amount
        userService.chargeBalance(user.id, amount)
        return ApiResponse.success()
    }
}