package kr.hhplus.be.server.domain.model.user

import kr.hhplus.be.server.support.error.CustomException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class UserWalletTest {

    @Test
    fun `0원은 충전할 수 없다`() {
        // given
        val userWallet = UserWallet(userId = 1L, balance = 0L)

        // when
        assertThatThrownBy {
            userWallet.charge(0L)
        }
            .isInstanceOf(CustomException::class.java)
            .hasMessage("잘못된 충전 금액입니다.")
    }

    @Test
    fun `마이너스 금액은 충전할 수 없다`() {
        // given
        val userWallet = UserWallet(userId = 1L, balance = 0L)

        // when
        assertThatThrownBy {
            userWallet.charge(-1000L)
        }
            .isInstanceOf(CustomException::class.java)
            .hasMessage("잘못된 충전 금액입니다.")
    }

    @Test
    fun `플러스 금액은 충전할 수 있다`() {
        // given
        val userWallet = UserWallet(userId = 1L, balance = 0L)

        // when
        userWallet.charge(1000L)

        // then
        assertThat(userWallet.balance).isEqualTo(1000L)
    }

    @Test
    fun `0원은 사용할 수 없다`() {
        // given
        val userWallet = UserWallet(userId = 1L, balance = 0L)

        // when
        assertThatThrownBy {
            userWallet.use(0L)
        }
            .isInstanceOf(CustomException::class.java)
            .hasMessage("잘못된 사용 금액입니다.")
    }

    @Test
    fun `마이너스 금액은 사용할 수 없다`() {
        // given
        val userWallet = UserWallet(userId = 1L, balance = 0L)

        // when
        assertThatThrownBy {
            userWallet.use(-1000L)
        }
            .isInstanceOf(CustomException::class.java)
            .hasMessage("잘못된 사용 금액입니다.")
    }

    @Test
    fun `잔액이 부족하면 사용할 수 없다`() {
        // given
        val userWallet = UserWallet(userId = 1L, balance = 0L)

        // when
        assertThatThrownBy {
            userWallet.use(1000L)
        }
            .isInstanceOf(CustomException::class.java)
            .hasMessage("잔액이 부족합니다.")
    }

    @Test
    fun `사용 금액 이상으로 잔액이 존재하면 사용할 수 없다`() {
        // given
        val userWallet = UserWallet(userId = 1L, balance = 1000L)

        // when
        userWallet.use(1000L)

        // then
        assertThat(userWallet.balance).isEqualTo(0L)
    }
}