package kr.hhplus.be.server.infrastructure.dao.user

import kr.hhplus.be.server.helper.CleanUp
import kr.hhplus.be.server.support.error.CustomException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UserWalletRepositoryImplTest(
    @Autowired private val cleanUp: CleanUp,
    @Autowired private val userWalletJpaRepository: UserWalletJpaRepository,
    @Autowired private val userWalletRepositoryImpl: UserWalletRepositoryImpl
) {
    @BeforeEach
    fun setUp() {
        cleanUp.all()
    }

    @Test
    fun `사용자의 지갑을 조회할 수 있다`() {
        // given
        userWalletJpaRepository.save(UserWalletEntity(userId = 2L, balance = 10000L))

        // when
        val result = userWalletRepositoryImpl.getByUserId(2L)

        // then
        assertThat(result.userId).isEqualTo(2L)
        assertThat(result.balance).isEqualTo(10000L)
    }

    @Test
    fun `사용자의 지갑이 존재하지 않으면 CustomException이 발생한다`() {
        // when then
        assertThatThrownBy {
            userWalletRepositoryImpl.getByUserId(2L)
        }
            .isInstanceOf(CustomException::class.java)
            .hasMessage("존재하지 않는 지갑입니다.")
    }
}