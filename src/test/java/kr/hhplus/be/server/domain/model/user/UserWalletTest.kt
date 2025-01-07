package kr.hhplus.be.server.domain.model.user

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class UserWalletTest {

    @Test
    fun `0원은 충전할 수 없다`() {
        // given
        val userWallet = UserWallet(1L, 0L)

        // when
        assertThatThrownBy {
            userWallet.charge(0L)
        }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("잘못된 충전 금액입니다.")
    }

    @Test
    fun `마이너스 금액은 충전할 수 없다`() {
        // given
        val userWallet = UserWallet(1L, 0L)

        // when
        assertThatThrownBy {
            userWallet.charge(-1000L)
        }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("잘못된 충전 금액입니다.")
    }

    @Test
    fun `플러스 금액은 충전할 수 있다`() {
        // given
        val userWallet = UserWallet(1L, 0L)

        // when
        userWallet.charge(1000L)

        // then
        assertThat(userWallet.balance).isEqualTo(1000L)
    }
}