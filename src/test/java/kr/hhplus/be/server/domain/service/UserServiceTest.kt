package kr.hhplus.be.server.domain.service

import kr.hhplus.be.server.domain.model.user.User
import kr.hhplus.be.server.domain.model.user.UserRepository
import kr.hhplus.be.server.helper.KSelect.Companion.field
import org.assertj.core.api.Assertions.assertThat
import org.instancio.Instancio
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock

class UserServiceTest {

    private lateinit var userRepository: UserRepository
    private lateinit var userService: UserService

    @BeforeEach
    fun setUp() {
        userRepository = mock()
        userService = UserService(userRepository)
    }

    @Test
    fun `사용자 정보를 조회할 수 있다`() {
        // given
        val user = Instancio.of(User::class.java)
            .set(field(User::id), 1L)
            .create()
        given(userRepository.getUserById(1L)).willReturn(user)

        // when
        val result = userService.getUserInfo(1L)

        // then
        assertThat(result.id).isEqualTo(user.id)
        assertThat(result.uuid).isEqualTo(user.uuid)
        assertThat(result.name).isEqualTo(user.name)
    }
}