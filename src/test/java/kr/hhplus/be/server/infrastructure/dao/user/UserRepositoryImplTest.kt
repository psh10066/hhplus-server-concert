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
class UserRepositoryImplTest(
    @Autowired private val cleanUp: CleanUp,
    @Autowired private val userJpaRepository: UserJpaRepository,
    @Autowired private val userRepositoryImpl: UserRepositoryImpl
) {
    @BeforeEach
    fun setUp() {
        cleanUp.all()
    }

    @Test
    fun `사용자를 조회할 수 있다`() {
        // given
        userJpaRepository.save(UserEntity(name = "홍길동"))

        // when
        val result = userRepositoryImpl.getUserById(1L)

        // then
        assertThat(result.id).isEqualTo(1L)
        assertThat(result.name).isEqualTo("홍길동")
    }

    @Test
    fun `사용자가 존재하지 않으면 CustomException이 발생한다`() {
        // when then
        assertThatThrownBy {
            userRepositoryImpl.getUserById(1L)
        }
            .isInstanceOf(CustomException::class.java)
            .hasMessage("존재하지 않는 유저입니다.")
    }
}