package kr.hhplus.be.server.support.config

import kr.hhplus.be.server.support.filter.LoggingFilter
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FilterConfig {

    @Bean
    fun loggingFilter(): FilterRegistrationBean<LoggingFilter> {
        val filterRegistrationBean = FilterRegistrationBean(LoggingFilter())
        filterRegistrationBean.addUrlPatterns("/api/*")
        return filterRegistrationBean
    }
}