package kr.hhplus.be.server.api.controller.v1.resolver

import jakarta.servlet.http.HttpServletRequest
import kr.hhplus.be.server.domain.model.user.User
import kr.hhplus.be.server.domain.service.UserService
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class UserArgumentResolver(
    private val userService: UserService
) : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.parameterType == User::class.java
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any? {
        val request = webRequest.getNativeRequest(HttpServletRequest::class.java)!!
        try {
            val userId = request.getHeader("userId") ?: throw IllegalArgumentException()
            return userService.getUser(userId.toLong())
        } catch (e: Exception) {
            throw IllegalArgumentException("접근이 거부되었습니다.")
        }
    }
}