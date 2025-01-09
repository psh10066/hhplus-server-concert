package kr.hhplus.be.server.api.controller.v1

import kr.hhplus.be.server.support.response.ApiResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ApiControllerAdvice {
    private val log: Logger = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(e: IllegalArgumentException): ResponseEntity<ApiResponse<Unit>> {
        log.error("IllegalArgumentException : {}", e.message, e)
        return ResponseEntity(ApiResponse.error(e.message), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(IllegalStateException::class)
    fun handleIllegalStateException(e: IllegalStateException): ResponseEntity<ApiResponse<Unit>> {
        log.error("IllegalStateException : {}", e.message, e)
        return ResponseEntity(ApiResponse.error(e.message), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<ApiResponse<Unit>> {
        log.error("Exception : {}", e.message, e)
        return ResponseEntity(ApiResponse.error(e.message), HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
