package kr.hhplus.be.server.api.config

import kr.hhplus.be.server.api.controller.v1.resolver.QueueInfoArgumentResolver
import kr.hhplus.be.server.api.controller.v1.resolver.UserInfoArgumentResolver
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig(
    private val userInfoArgumentResolver: UserInfoArgumentResolver,
    private val queueInfoArgumentResolver: QueueInfoArgumentResolver
) : WebMvcConfigurer {

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(userInfoArgumentResolver)
        resolvers.add(queueInfoArgumentResolver)
    }
}