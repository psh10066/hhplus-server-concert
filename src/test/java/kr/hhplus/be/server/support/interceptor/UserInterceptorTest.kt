package kr.hhplus.be.server.support.interceptor

import kr.hhplus.be.server.domain.model.user.User
import kr.hhplus.be.server.domain.service.UserService
import kr.hhplus.be.server.support.error.CustomException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import org.springframework.mock.web.MockHttpServletRequest
import java.util.*

class UserInterceptorTest {

    private lateinit var httpServletRequest: MockHttpServletRequest
    private lateinit var userService: UserService
    private lateinit var userInterceptor: UserInterceptor

    @BeforeEach
    fun setUp() {
        httpServletRequest = MockHttpServletRequest()
        userService = mock()
        userInterceptor = UserInterceptor(userService)
    }

    @Test
    fun `헤더에 userId가 없으면 예외가 발생한다`() {
        // when then
        assertThatThrownBy {
            userInterceptor.preHandle(httpServletRequest, mock(), mock())
        }
            .isInstanceOf(CustomException::class.java)
            .hasMessage("접근이 거부되었습니다.")
    }

    @Test
    fun `헤더의 userId에 해당하는 사용자가 없으면 예외가 발생한다`() {
        // given
        httpServletRequest.addHeader("userId", "1")
        given(userService.getUser(1L)).willThrow(CustomException::class.java)

        assertThatThrownBy {
            userInterceptor.preHandle(httpServletRequest, mock(), mock())
        }
            .isInstanceOf(CustomException::class.java)
            .hasMessage("접근이 거부되었습니다.")
    }

    @Test
    fun `헤더의 userId에 해당하는 사용자가 있으면 유저를 request에 추가한다`() {
        // given
        httpServletRequest.addHeader("userId", "1")
        val user = User(id = 1L, uuid = UUID.randomUUID(), name = "홍길동")
        given(userService.getUser(1L)).willReturn(user)

        // when
        val result = userInterceptor.preHandle(httpServletRequest, mock(), mock())

        // then
        assertThat(result).isTrue()
        assertThat(httpServletRequest.getAttribute("user")).isEqualTo(user)
    }
}