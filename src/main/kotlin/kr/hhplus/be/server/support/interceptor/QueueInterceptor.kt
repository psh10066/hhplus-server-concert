package kr.hhplus.be.server.support.interceptor

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.hhplus.be.server.domain.service.QueueService
import kr.hhplus.be.server.support.error.CustomException
import kr.hhplus.be.server.support.error.ErrorType
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Component
class QueueInterceptor(
    private val queueService: QueueService
) : HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        try {
            val token = request.getHeader("token") ?: throw IllegalArgumentException()
            val queue = queueService.getActiveQueue(token)
            request.setAttribute("queue", queue)
            return true
        } catch (e: Exception) {
            throw CustomException(ErrorType.UNAUTHORIZED)
        }
    }
}