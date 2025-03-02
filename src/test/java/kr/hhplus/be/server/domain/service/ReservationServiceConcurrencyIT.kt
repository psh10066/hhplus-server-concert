package kr.hhplus.be.server.domain.service

import kr.hhplus.be.server.domain.model.queue.Queue
import kr.hhplus.be.server.domain.model.user.User
import kr.hhplus.be.server.helper.CleanUp
import kr.hhplus.be.server.infrastructure.dao.concert.*
import kr.hhplus.be.server.infrastructure.dao.queue.QueueRedisRepository
import kr.hhplus.be.server.infrastructure.dao.reservation.ReservationJpaRepository
import kr.hhplus.be.server.infrastructure.dao.user.UserEntity
import kr.hhplus.be.server.infrastructure.dao.user.UserJpaRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import kotlin.time.Duration.Companion.minutes

@SpringBootTest
class ReservationServiceConcurrencyIT(
    @Autowired private val userJpaRepository: UserJpaRepository,
    @Autowired private val concertJpaRepository: ConcertJpaRepository,
    @Autowired private val concertScheduleJpaRepository: ConcertScheduleJpaRepository,
    @Autowired private val concertSeatJpaRepository: ConcertSeatJpaRepository,
    @Autowired private val queueRedisRepository: QueueRedisRepository,
    @Autowired private val reservationJpaRepository: ReservationJpaRepository,
    @Autowired private val reservationService: ReservationService,
    @Autowired private val cleanUp: CleanUp
) {

    @BeforeEach
    fun setUp() {
        cleanUp.all()
    }

    @Test
    fun `동시에 5명이 같은 좌석에 예약 요청 시 1명만 예약되어야 한다`() {
        // given
        val users = mutableListOf<User>()
        for (i in 1..5) {
            val user = userJpaRepository.save(UserEntity(name = "user$i")).toModel()
            users.add(user)
            queueRedisRepository.addWaitingToken(Queue.create(user.uuid).token, i.toDouble())
        }
        queueRedisRepository.activateTokens(5, 5.minutes)
        concertJpaRepository.save(ConcertEntity(name = "아이유 콘서트", price = 150000L))
        concertScheduleJpaRepository.save(ConcertScheduleEntity(concertId = 1L, startTime = LocalDate.now().plusDays(1).atTime(11, 0)))
        val concertSeat = concertSeatJpaRepository.save(ConcertSeatEntity(concertScheduleId = 1L, seatNumber = 12))

        // when
        concurrencyTestHelper(1, *users.map { user ->
            Runnable {
                reservationService.concertReservation(user, concertSeat.id)
            }
        }.toTypedArray())

        // then
        val reservationCount = reservationJpaRepository.count()
        assertThat(reservationCount).isEqualTo(1)
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
            runCatching { CompletableFuture.allOf(*futures.toTypedArray()).join() }
        } finally {
            executorService.shutdown()
        }
    }
}