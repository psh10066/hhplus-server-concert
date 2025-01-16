package kr.hhplus.be.server.domain.service

import kr.hhplus.be.server.domain.model.user.*
import kr.hhplus.be.server.helper.KSelect.Companion.field
import org.assertj.core.api.Assertions.assertThat
import org.instancio.Instancio
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.argumentCaptor

class UserServiceTest {

    private lateinit var userRepository: UserRepository
    private lateinit var userWalletRepository: UserWalletRepository
    private lateinit var userWalletHistoryRepository: UserWalletHistoryRepository
    private lateinit var userService: UserService

    @BeforeEach
    fun setUp() {
        userRepository = mock()
        userWalletRepository = mock()
        userWalletHistoryRepository = mock()
        userService = UserService(userRepository, userWalletRepository, userWalletHistoryRepository)
    }

    @Test
    fun `사용자 정보를 조회할 수 있다`() {
        // given
        val user = Instancio.of(User::class.java)
            .set(field(User::id), 1L)
            .create()
        given(userRepository.getUserById(1L)).willReturn(user)

        // when
        val result = userService.getUser(1L)

        // then
        assertThat(result.id).isEqualTo(user.id)
        assertThat(result.uuid).isEqualTo(user.uuid)
        assertThat(result.name).isEqualTo(user.name)
    }

    @Test
    fun `사용자 잔고를 조회할 수 있다`() {
        // given
        val userWallet = createUserWallet(1L, 10000L)
        given(userWalletRepository.getByUserId(1L)).willReturn(userWallet)

        // when
        val result = userService.getBalanceByUserId(1L)

        // then
        assertThat(result).isEqualTo(10000L)
    }

    @Test
    fun `사용자 잔고를 충전할 수 있다`() {
        // given
        val userWallet = createUserWallet(1L, 10000L)
        given(userWalletRepository.getByUserIdWithLock(1L)).willReturn(userWallet)

        // when
        userService.chargeBalance(1L, 5000L)

        // then
        assertThat(userWallet.balance).isEqualTo(15000L)
        verify(userWalletRepository).save(userWallet)
    }

    @Test
    fun `사용자 잔고 충전 시 충전 이력을 저장할 수 있다`() {
        // given
        val userWallet = createUserWallet(1L, 10000L)
        given(userWalletRepository.getByUserIdWithLock(1L)).willReturn(userWallet)

        // when
        userService.chargeBalance(1L, 5000L)

        // then
        val captor = argumentCaptor<UserWalletHistory>()
        verify(userWalletHistoryRepository).save(captor.capture())

        val userWalletHistory = captor.firstValue
        assertThat(userWalletHistory.userWalletId).isEqualTo(userWallet.id)
        assertThat(userWalletHistory.amount).isEqualTo(5000L)
    }

    @Test
    fun `사용자 잔고를 사용할 수 있다`() {
        // given
        val userWallet = createUserWallet(1L, 10000L)
        given(userWalletRepository.getByUserIdWithLock(1L)).willReturn(userWallet)

        // when
        userService.useBalance(1L, 5000L)

        // then
        assertThat(userWallet.balance).isEqualTo(5000L)
        verify(userWalletRepository).save(userWallet)
    }

    @Test
    fun `사용자 잔고 사용 시 충전 이력을 사용할 수 있다`() {
        // given
        val userWallet = createUserWallet(1L, 10000L)
        given(userWalletRepository.getByUserIdWithLock(1L)).willReturn(userWallet)

        // when
        userService.useBalance(1L, 5000L)

        // then
        val captor = argumentCaptor<UserWalletHistory>()
        verify(userWalletHistoryRepository).save(captor.capture())

        val userWalletHistory = captor.firstValue
        assertThat(userWalletHistory.userWalletId).isEqualTo(userWallet.id)
        assertThat(userWalletHistory.amount).isEqualTo(-5000L)
    }

    private fun createUserWallet(userId: Long, balance: Long): UserWallet {
        return Instancio.of(UserWallet::class.java)
            .set(field(UserWallet::userId), userId)
            .set(field(UserWallet::balance), balance)
            .create()
    }
}