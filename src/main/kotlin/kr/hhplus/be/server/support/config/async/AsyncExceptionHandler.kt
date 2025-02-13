package kr.hhplus.be.server.support.config.async

import kr.hhplus.be.server.support.error.CustomException
import org.slf4j.LoggerFactory
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler
import org.springframework.boot.logging.LogLevel
import java.lang.reflect.Method

class AsyncExceptionHandler : AsyncUncaughtExceptionHandler {
    private val log = LoggerFactory.getLogger(javaClass)

    override fun handleUncaughtException(e: Throwable, method: Method, vararg params: Any?) {
        if (e is CustomException) {
            when (e.errorType.logLevel) {
                LogLevel.ERROR -> log.error("CustomException : ${e.message}", e)
                LogLevel.WARN -> log.warn("CustomException : ${e.message}", e)
                else -> log.info("CustomException : ${e.message}", e)
            }
        } else {
            log.error("Exception : {}", e.message, e)
        }
    }
}
