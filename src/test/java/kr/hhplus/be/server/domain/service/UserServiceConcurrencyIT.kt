package kr.hhplus.be.server.domain.service

import kr.hhplus.be.server.helper.CleanUp
import kr.hhplus.be.server.infrastructure.dao.user.UserWalletEntity
import kr.hhplus.be.server.infrastructure.dao.user.UserWalletJpaRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors

@SpringBootTest
class UserServiceConcurrencyIT(
    @Autowired private val userWalletJpaRepository: UserWalletJpaRepository,
    @Autowired private val userService: UserService,
    @Autowired private val cleanUp: CleanUp
) {

    @BeforeEach
    fun setUp() {
        cleanUp.all()
    }

    @Test
    fun `동시에 5번 충전 요청 시 문제없이 충전되어야 한다`() {
        // given
        userWalletJpaRepository.save(UserWalletEntity(userId = 1L, balance = 0L))

        // when
        concurrencyTestHelper(5, Runnable {
            userService.chargeBalance(1L, 100L)
        })

        // then
        val userWallet = userWalletJpaRepository.findByUserId(1)!!
        assertThat(userWallet.balance).isEqualTo(500L)
    }

    @Test
    fun `동시에 5번 사용 요청 시 문제없이 사용되어야 한다`() {
        // given
        userWalletJpaRepository.save(UserWalletEntity(userId = 1L, balance = 500L))

        // when
        concurrencyTestHelper(5, Runnable {
            userService.useBalance(1L, 100L)
        })

        // then
        val userWallet = userWalletJpaRepository.findByUserId(1)!!
        assertThat(userWallet.balance).isEqualTo(0L)
    }

    @Test
    fun `동시에 충전과 사용 각각 5번씩 요청 이후 잔고의 정합성이 보장되어야 한다`() {
        // given
        userWalletJpaRepository.save(UserWalletEntity(userId = 1L, balance = 500L))

        // when
        concurrencyTestHelper(1,
            Runnable { userService.chargeBalance(1L, 200L) },
            Runnable { userService.useBalance(1L, 100L) },
            Runnable { userService.chargeBalance(1L, 200L) },
            Runnable { userService.chargeBalance(1L, 200L) },
            Runnable { userService.useBalance(1L, 100L) },
            Runnable { userService.chargeBalance(1L, 200L) },
            Runnable { userService.useBalance(1L, 100L) },
            Runnable { userService.chargeBalance(1L, 200L) },
            Runnable { userService.useBalance(1L, 100L) },
            Runnable { userService.useBalance(1L, 100L) },
        )

        // then
        val userWallet = userWalletJpaRepository.findByUserId(1)!!
        assertThat(userWallet.balance).isEqualTo(1000L)
    }

    private fun concurrencyTestHelper(times: Int, vararg tasks: Runnable) {
        val executorService = Executors.newFixedThreadPool(times * tasks.size)
        try {
            // 각 task를 times번씩 실행할 CompletableFuture를 한 번에 생성
            val futures = (1..times).flatMap {
                tasks.map { task ->
                    CompletableFuture.runAsync(task, executorService)
                }
            }

            // 생성된 모든 Future가 끝날 때까지 대기
            CompletableFuture.allOf(*futures.toTypedArray()).join()
        } finally {
            executorService.shutdown()
        }
    }
}