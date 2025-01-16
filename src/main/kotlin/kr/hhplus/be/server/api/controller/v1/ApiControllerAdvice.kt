package kr.hhplus.be.server.api.controller.v1

import kr.hhplus.be.server.support.error.CustomException
import kr.hhplus.be.server.support.error.ErrorType
import kr.hhplus.be.server.support.response.ApiResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.logging.LogLevel
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ApiControllerAdvice {
    private val log: Logger = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(CustomException::class)
    fun handleCustomException(e: CustomException): ResponseEntity<ApiResponse<Unit>> {
        when (e.errorType.logLevel) {
            LogLevel.ERROR -> log.error("CustomException : ${e.message}", e)
            LogLevel.WARN -> log.warn("CustomException : ${e.message}", e)
            else -> log.info("CustomException : ${e.message}", e)
        }
        return ResponseEntity(ApiResponse.error(e.message), e.errorType.status)
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<ApiResponse<Unit>> {
        log.error("Exception : {}", e.message, e)
        return ResponseEntity(ApiResponse.error(ErrorType.DEFAULT_ERROR.message), ErrorType.DEFAULT_ERROR.status)
    }
}
