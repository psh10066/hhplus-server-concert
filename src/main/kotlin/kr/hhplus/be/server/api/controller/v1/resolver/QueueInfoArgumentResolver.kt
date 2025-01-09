package kr.hhplus.be.server.api.controller.v1.resolver

import jakarta.servlet.http.HttpServletRequest
import kr.hhplus.be.server.domain.model.queue.dto.QueueInfo
import kr.hhplus.be.server.domain.service.QueueService
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class QueueInfoArgumentResolver(
    private val queueService: QueueService
) : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.parameterType == QueueInfo::class.java
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any? {
        val request = webRequest.getNativeRequest(HttpServletRequest::class.java)!!
        try {
            val token = request.getHeader("token") ?: throw IllegalArgumentException()
            return queueService.getActiveQueue(token)
        } catch (e: Exception) {
            throw IllegalArgumentException("접근이 거부되었습니다.")
        }
    }
}