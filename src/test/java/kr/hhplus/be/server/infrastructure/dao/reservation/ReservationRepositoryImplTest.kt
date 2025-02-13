package kr.hhplus.be.server.infrastructure.dao.reservation

import kr.hhplus.be.server.helper.CleanUp
import kr.hhplus.be.server.helper.KSelect.Companion.field
import org.assertj.core.api.Assertions.assertThat
import org.instancio.Instancio
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@SpringBootTest
class ReservationRepositoryImplTest(
    @Autowired private val cleanUp: CleanUp,
    @Autowired private val reservationJpaRepository: ReservationJpaRepository,
    @Autowired private val reservationRepositoryImpl: ReservationRepositoryImpl
) {

    @BeforeEach
    fun setUp() {
        cleanUp.all()
    }

    @Test
    fun `콘서트 당 예약의 개수를 조회할 수 있다`() {
        // given
        val today = LocalDate.now()
        reservationJpaRepository.save(createReservation(concertId = 1, createdAt = today)) // concert1
        reservationJpaRepository.save(createReservation(concertId = 2, createdAt = today)) // concert1
        reservationJpaRepository.save(createReservation(concertId = 3, createdAt = today)) // concert2
        reservationJpaRepository.save(createReservation(concertId = 2, createdAt = today)) // concert2
        reservationJpaRepository.save(createReservation(concertId = 1, createdAt = today)) // concert2
        reservationJpaRepository.save(createReservation(concertId = 1, createdAt = today)) // concert3

        // when
        val result = reservationRepositoryImpl.findConcertReservationCountsByDate(today, 20)

        // then
        assertThat(result).hasSize(3)
        assertThat(result.find { it.concertId == 1L }!!.count).isEqualTo(3)
        assertThat(result.find { it.concertId == 2L }!!.count).isEqualTo(2)
        assertThat(result.find { it.concertId == 3L }!!.count).isEqualTo(1)
    }

    private fun createReservation(concertId: Long, createdAt: LocalDate): ReservationEntity {
        return Instancio.of(ReservationEntity::class.java)
            .set(field(ReservationEntity::id), 0)
            .set(field(ReservationEntity::concertId), concertId)
            .set(field(ReservationEntity::expiredAt), LocalDateTime.now().plusMinutes(2))
            .set(field(ReservationEntity::createdAt), createdAt.atTime(LocalTime.of(12, 0)))
            .create()
    }
}