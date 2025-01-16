package kr.hhplus.be.server.support.interceptor

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.hhplus.be.server.domain.service.UserService
import kr.hhplus.be.server.support.error.CustomException
import kr.hhplus.be.server.support.error.ErrorType
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Component
class UserInterceptor(
    private val userService: UserService
) : HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        try {
            val userId = request.getHeader("userId") ?: throw IllegalArgumentException()
            val user = userService.getUser(userId.toLong())
            request.setAttribute("user", user)
            return true
        } catch (e: Exception) {
            throw CustomException(ErrorType.UNAUTHORIZED)
        }
    }
}