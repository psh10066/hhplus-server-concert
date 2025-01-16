package kr.hhplus.be.server.support.interceptor

import kr.hhplus.be.server.domain.model.queue.Queue
import kr.hhplus.be.server.domain.model.queue.QueueStatus
import kr.hhplus.be.server.domain.service.QueueService
import kr.hhplus.be.server.support.error.CustomException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import org.springframework.mock.web.MockHttpServletRequest
import java.time.LocalDateTime
import java.util.*

class QueueInterceptorTest {

    private lateinit var httpServletRequest: MockHttpServletRequest
    private lateinit var queueService: QueueService
    private lateinit var queueInterceptor: QueueInterceptor

    @BeforeEach
    fun setUp() {
        httpServletRequest = MockHttpServletRequest()
        queueService = mock()
        queueInterceptor = QueueInterceptor(queueService)
    }

    @Test
    fun `헤더에 token이 없으면 예외가 발생한다`() {
        // when then
        assertThatThrownBy {
            queueInterceptor.preHandle(httpServletRequest, mock(), mock())
        }
            .isInstanceOf(CustomException::class.java)
            .hasMessage("접근이 거부되었습니다.")
    }

    @Test
    fun `헤더의 token으로 활성화된 대기열을 조회할 수 없으면 예외가 발생한다`() {
        // given
        val token = "token:123"
        httpServletRequest.addHeader("token", token)
        given(queueService.getActiveQueue(token)).willThrow(CustomException::class.java)

        // when then
        assertThatThrownBy {
            queueInterceptor.preHandle(httpServletRequest, mock(), mock())
        }
            .isInstanceOf(CustomException::class.java)
            .hasMessage("접근이 거부되었습니다.")
    }

    @Test
    fun `헤더의 token으로 활성화된 대기열을 조회할 수 있으면 정상적으로 반환한다`() {
        // given
        val token = "token:123"
        httpServletRequest.addHeader("token", token)

        val queue = Queue(
            id = 1L,
            userUuid = UUID.randomUUID(),
            status = QueueStatus.ACTIVE,
            token = token,
            expiredAt = LocalDateTime.now().plusMinutes(10)
        )
        given(queueService.getActiveQueue(token)).willReturn(queue)

        // when
        val result = queueInterceptor.preHandle(httpServletRequest, mock(), mock())

        // then
        assertThat(result).isTrue()
    }
}