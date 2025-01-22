package kr.hhplus.be.server.api.config

import kr.hhplus.be.server.api.controller.v1.resolver.UserArgumentResolver
import kr.hhplus.be.server.support.interceptor.QueueValidInterceptor
import kr.hhplus.be.server.support.interceptor.UserInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig(
    private val userArgumentResolver: UserArgumentResolver,
    private val userInterceptor: UserInterceptor,
    private val queueValidInterceptor: QueueValidInterceptor,
) : WebMvcConfigurer {

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(userArgumentResolver)
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(userInterceptor)
            .addPathPatterns("/api/v1/user-wallets/**")
            .addPathPatterns("/api/v1/queues/**")

        registry.addInterceptor(queueValidInterceptor)
            .addPathPatterns("/api/v1/concerts/**")
            .addPathPatterns("/api/v1/reservations/**")
    }
}