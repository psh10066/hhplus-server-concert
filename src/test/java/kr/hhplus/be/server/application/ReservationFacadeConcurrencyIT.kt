package kr.hhplus.be.server.application

import kr.hhplus.be.server.domain.model.queue.QueueStatus
import kr.hhplus.be.server.domain.model.user.User
import kr.hhplus.be.server.helper.CleanUp
import kr.hhplus.be.server.infrastructure.dao.concert.ConcertSeatEntity
import kr.hhplus.be.server.infrastructure.dao.concert.ConcertSeatJpaRepository
import kr.hhplus.be.server.infrastructure.dao.queue.QueueEntity
import kr.hhplus.be.server.infrastructure.dao.queue.QueueJpaRepository
import kr.hhplus.be.server.infrastructure.dao.reservation.ReservationJpaRepository
import kr.hhplus.be.server.infrastructure.dao.user.UserEntity
import kr.hhplus.be.server.infrastructure.dao.user.UserJpaRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors

@SpringBootTest
class ReservationFacadeConcurrencyIT(
    @Autowired private val userJpaRepository: UserJpaRepository,
    @Autowired private val queueJpaRepository: QueueJpaRepository,
    @Autowired private val concertSeatJpaRepository: ConcertSeatJpaRepository,
    @Autowired private val reservationJpaRepository: ReservationJpaRepository,
    @Autowired private val reservationFacade: ReservationFacade,
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
            queueJpaRepository.save(
                QueueEntity(
                    userUuid = user.uuid,
                    status = QueueStatus.ACTIVE,
                    token = "token:$i",
                    expiredAt = LocalDateTime.now().plusMinutes(10)
                )
            )
        }
        val concertSeat = concertSeatJpaRepository.save(ConcertSeatEntity(concertScheduleId = 1L, seatNumber = 12))

        // when
        concurrencyTestHelper(1, *users.map { user ->
            Runnable {
                reservationFacade.concertReservation(user, concertSeat.id)
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