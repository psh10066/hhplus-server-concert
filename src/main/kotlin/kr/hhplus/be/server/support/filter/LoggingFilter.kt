package kr.hhplus.be.server.support.filter

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.MDC
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.MediaType
import org.springframework.util.StreamUtils
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingResponseWrapper
import java.io.InputStream
import java.nio.charset.StandardCharsets
import java.util.*

@Order(Ordered.HIGHEST_PRECEDENCE)
class LoggingFilter : OncePerRequestFilter() {
    private val objectMapper = ObjectMapper()

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val requestWrapper = CustomCachingRequestWrapper(request)
        val responseWrapper = ContentCachingResponseWrapper(response)

        MDC.put("requestId", UUID.randomUUID().toString().split("-")[0])
        logRequest(requestWrapper)

        filterChain.doFilter(requestWrapper, responseWrapper)

        logResponse(responseWrapper)
        MDC.clear()
    }

    private fun logRequest(request: HttpServletRequest) {
        val queryString = request.queryString
        val requestURI = "${request.requestURI}${queryString?.let { "?$it" } ?: ""}"
        val requestBody = minifyJson(getBody(request.inputStream))

        logger.info("[${MDC.get("requestId")}] Request  : ${request.method} URI=[$requestURI] BODY=[$requestBody]")
    }

    private fun logResponse(responseWrapper: ContentCachingResponseWrapper) {
        if (responseWrapper.contentType != MediaType.APPLICATION_JSON_VALUE) {
            return
        }

        val responseBody = getBody(responseWrapper.contentInputStream)

        logger.info("[${MDC.get("requestId")}] Response : HttpStatus=[${responseWrapper.status}] Body=[$responseBody]")

        responseWrapper.copyBodyToResponse()
    }

    private fun getBody(inputStream: InputStream): String {
        val content = StreamUtils.copyToByteArray(inputStream)
        return String(content, StandardCharsets.UTF_8)
    }

    private fun minifyJson(json: String): String {
        return objectMapper.readTree(json).toString()
    }
}