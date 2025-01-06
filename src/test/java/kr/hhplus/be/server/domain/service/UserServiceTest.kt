package kr.hhplus.be.server.domain.service

import kr.hhplus.be.server.domain.model.user.User
import kr.hhplus.be.server.domain.model.user.UserRepository
import kr.hhplus.be.server.domain.model.user.UserWallet
import kr.hhplus.be.server.domain.model.user.UserWalletRepository
import kr.hhplus.be.server.helper.KSelect.Companion.field
import org.assertj.core.api.Assertions.assertThat
import org.instancio.Instancio
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock

class UserServiceTest {

    private lateinit var userRepository: UserRepository
    private lateinit var userWalletRepository: UserWalletRepository
    private lateinit var userService: UserService

    @BeforeEach
    fun setUp() {
        userRepository = mock()
        userWalletRepository = mock()
        userService = UserService(userRepository, userWalletRepository)
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

    @Test
    fun `사용자 잔고를 조회할 수 있다`() {
        // given
        val userWallet = Instancio.of(UserWallet::class.java)
            .set(field(UserWallet::userId), 1L)
            .set(field(UserWallet::balance), 10000L)
            .create()
        given(userWalletRepository.getByUserId(1L)).willReturn(userWallet)

        // when
        val result = userService.getBalanceByUserId(1L)

        // then
        assertThat(result).isEqualTo(10000L)
    }
}