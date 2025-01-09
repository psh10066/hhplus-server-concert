package kr.hhplus.be.server.api.controller.v1.resolver

import jakarta.servlet.http.HttpServletRequest
import kr.hhplus.be.server.domain.model.queue.QueueStatus
import kr.hhplus.be.server.domain.model.queue.dto.QueueInfo
import kr.hhplus.be.server.domain.service.QueueService
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.web.context.request.NativeWebRequest
import java.time.LocalDateTime
import java.util.*

class QueueInfoArgumentResolverTest {

    private lateinit var webRequest: NativeWebRequest
    private lateinit var queueService: QueueService
    private lateinit var queueInfoArgumentResolver: QueueInfoArgumentResolver

    @BeforeEach
    fun setUp() {
        webRequest = mock()
        queueService = mock()
        queueInfoArgumentResolver = QueueInfoArgumentResolver(queueService)
    }

    @Test
    fun `헤더에 token이 없으면 예외가 발생한다`() {
        // given
        val httpServletRequest = MockHttpServletRequest()
        given(webRequest.getNativeRequest(HttpServletRequest::class.java)).willReturn(httpServletRequest)

        // when then
        assertThatThrownBy {
            queueInfoArgumentResolver.resolveArgument(mock(), mock(), webRequest, mock())
        }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("접근이 거부되었습니다.")
    }

    @Test
    fun `헤더의 token으로 활성화된 대기열을 조회할 수 없으면 예외가 발생한다`() {
        // given
        val httpServletRequest = MockHttpServletRequest()
        val token = "token:123"
        httpServletRequest.addHeader("token", token)
        given(webRequest.getNativeRequest(HttpServletRequest::class.java)).willReturn(httpServletRequest)
        given(queueService.getActiveQueue(token)).willThrow(IllegalArgumentException::class.java)

        // when then
        assertThatThrownBy {
            queueInfoArgumentResolver.resolveArgument(mock(), mock(), webRequest, mock())
        }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("접근이 거부되었습니다.")
    }

    @Test
    fun `헤더의 token으로 활성화된 대기열을 조회할 수 있으면 정상적으로 반환한다`() {
        // given
        val httpServletRequest = MockHttpServletRequest()
        val token = "token:123"
        httpServletRequest.addHeader("token", token)
        given(webRequest.getNativeRequest(HttpServletRequest::class.java)).willReturn(httpServletRequest)

        val queueInfo = QueueInfo(
            id = 1L,
            userUuid = UUID.randomUUID(),
            status = QueueStatus.ACTIVE,
            token = "token:123",
            expiredAt = LocalDateTime.now().plusMinutes(10)
        )
        given(queueService.getActiveQueue(token)).willReturn(queueInfo)

        // when
        val result = queueInfoArgumentResolver.resolveArgument(mock(), mock(), webRequest, mock())

        // then
        assertThat(result).isEqualTo(queueInfo)
    }
}