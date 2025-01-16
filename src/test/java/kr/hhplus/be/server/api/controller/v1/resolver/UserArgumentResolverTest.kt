package kr.hhplus.be.server.api.controller.v1.resolver

import jakarta.servlet.http.HttpServletRequest
import kr.hhplus.be.server.domain.model.user.User
import kr.hhplus.be.server.domain.service.UserService
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.web.context.request.NativeWebRequest
import java.util.*

class UserArgumentResolverTest {

    private lateinit var webRequest: NativeWebRequest
    private lateinit var userService: UserService
    private lateinit var userArgumentResolver: UserArgumentResolver

    @BeforeEach
    fun setUp() {
        webRequest = mock()
        userService = mock()
        userArgumentResolver = UserArgumentResolver(userService)
    }

    @Test
    fun `헤더에 userId가 없으면 예외가 발생한다`() {
        // given
        val httpServletRequest = MockHttpServletRequest()
        given(webRequest.getNativeRequest(HttpServletRequest::class.java)).willReturn(httpServletRequest)

        // when then
        assertThatThrownBy {
            userArgumentResolver.resolveArgument(mock(), mock(), webRequest, mock())
        }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("접근이 거부되었습니다.")
    }

    @Test
    fun `헤더의 userId에 해당하는 사용자가 없으면 예외가 발생한다`() {
        // given
        val httpServletRequest = MockHttpServletRequest()
        httpServletRequest.addHeader("userId", "1")
        given(webRequest.getNativeRequest(HttpServletRequest::class.java)).willReturn(httpServletRequest)
        given(userService.getUser(1L)).willThrow(IllegalArgumentException::class.java)

        // when then
        assertThatThrownBy {
            userArgumentResolver.resolveArgument(mock(), mock(), webRequest, mock())
        }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("접근이 거부되었습니다.")
    }

    @Test
    fun `헤더의 userId에 해당하는 사용자가 있으면 정상적으로 반환한다`() {
        // given
        val httpServletRequest = MockHttpServletRequest()
        httpServletRequest.addHeader("userId", "1")
        given(webRequest.getNativeRequest(HttpServletRequest::class.java)).willReturn(httpServletRequest)
        given(userService.getUser(1L)).willReturn(User(id = 1L, uuid = UUID.randomUUID(), name = "홍길동"))

        // when
        val result = userArgumentResolver.resolveArgument(mock(), mock(), webRequest, mock())

        // then
        val user = result as User
        assertThat(user.id).isEqualTo(1L)
        assertThat(user.name).isEqualTo("홍길동")
    }
}