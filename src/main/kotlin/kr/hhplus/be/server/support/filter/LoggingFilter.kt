package kr.hhplus.be.server.support.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper

class LoggingFilter : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val requestWrapper = ContentCachingRequestWrapper(request)
        val responseWrapper = ContentCachingResponseWrapper(response)

        filterChain.doFilter(requestWrapper, responseWrapper)

        logRequest(requestWrapper)
        logResponse(responseWrapper)
    }

    private fun logRequest(requestWrapper: ContentCachingRequestWrapper) {
        logger.info("Request URI: ${requestWrapper.requestURI}")
        logger.info("Request Method: ${requestWrapper.method}")

        val queryString = requestWrapper.queryString
        if (queryString?.isNotBlank() == true) {
            logger.info("Request Param: $queryString")
        }

        val responseBody = requestWrapper.contentAsByteArray.decodeToString()
        if (responseBody.isNotBlank()) {
            logger.info("Request Body: $responseBody")
        }
    }

    private fun logResponse(responseWrapper: ContentCachingResponseWrapper) {
        if (responseWrapper.contentType != MediaType.APPLICATION_JSON_VALUE) {
            return
        }
        logger.info("Response Body: ${responseWrapper.contentAsByteArray.decodeToString()}")
        responseWrapper.copyBodyToResponse()
    }
}