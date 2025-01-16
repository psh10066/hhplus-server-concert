package kr.hhplus.be.server.api.config

import kr.hhplus.be.server.api.controller.v1.resolver.QueueArgumentResolver
import kr.hhplus.be.server.api.controller.v1.resolver.UserArgumentResolver
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig(
    private val userArgumentResolver: UserArgumentResolver,
    private val queueArgumentResolver: QueueArgumentResolver
) : WebMvcConfigurer {

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(userArgumentResolver)
        resolvers.add(queueArgumentResolver)
    }
}