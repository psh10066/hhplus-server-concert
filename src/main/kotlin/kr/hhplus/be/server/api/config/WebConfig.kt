package kr.hhplus.be.server.api.config

import kr.hhplus.be.server.api.controller.v1.resolver.QueueArgumentResolver
import kr.hhplus.be.server.api.controller.v1.resolver.UserArgumentResolver
import kr.hhplus.be.server.support.interceptor.QueueInterceptor
import kr.hhplus.be.server.support.interceptor.UserInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig(
    private val userArgumentResolver: UserArgumentResolver,
    private val userInterceptor: UserInterceptor,
    private val queueArgumentResolver: QueueArgumentResolver,
    private val queueInterceptor: QueueInterceptor,
) : WebMvcConfigurer {

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(userArgumentResolver)
        resolvers.add(queueArgumentResolver)
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(userInterceptor)
            .addPathPatterns("/api/v1/user-wallets/**")
            .addPathPatterns("/api/v1/queues/**")

        registry.addInterceptor(queueInterceptor)
            .addPathPatterns("/api/v1/concerts/**")
            .addPathPatterns("/api/v1/reservations/**")
    }
}