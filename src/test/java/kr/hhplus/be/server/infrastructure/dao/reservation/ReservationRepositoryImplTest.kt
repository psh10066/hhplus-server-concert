package kr.hhplus.be.server.infrastructure.dao.reservation

import kr.hhplus.be.server.helper.CleanUp
import kr.hhplus.be.server.helper.KSelect.Companion.field
import kr.hhplus.be.server.infrastructure.dao.concert.ConcertScheduleEntity
import kr.hhplus.be.server.infrastructure.dao.concert.ConcertScheduleJpaRepository
import kr.hhplus.be.server.infrastructure.dao.concert.ConcertSeatEntity
import kr.hhplus.be.server.infrastructure.dao.concert.ConcertSeatJpaRepository
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
    @Autowired private val concertScheduleJpaRepository: ConcertScheduleJpaRepository,
    @Autowired private val concertSeatJpaRepository: ConcertSeatJpaRepository,
    @Autowired private val reservationJpaRepository: ReservationJpaRepository,
    @Autowired private val reservationRepositoryImpl: ReservationRepositoryImpl
) {

    @BeforeEach
    fun setUp() {
        cleanUp.all()
        val concert1Schedule1 = concertScheduleJpaRepository.save(createConcertScheduleEntity(concertId = 1))
        val concert1Schedule2 = concertScheduleJpaRepository.save(createConcertScheduleEntity(concertId = 1))
        val concert2Schedule1 = concertScheduleJpaRepository.save(createConcertScheduleEntity(concertId = 2))
        val concert3Schedule1 = concertScheduleJpaRepository.save(createConcertScheduleEntity(concertId = 3))
        val concert4Schedule1 = concertScheduleJpaRepository.save(createConcertScheduleEntity(concertId = 4))
        concertSeatJpaRepository.save(ConcertSeatEntity(concertScheduleId = concert1Schedule1.id, seatNumber = 1))
        concertSeatJpaRepository.save(ConcertSeatEntity(concertScheduleId = concert1Schedule2.id, seatNumber = 1))
        concertSeatJpaRepository.save(ConcertSeatEntity(concertScheduleId = concert2Schedule1.id, seatNumber = 1))
        concertSeatJpaRepository.save(ConcertSeatEntity(concertScheduleId = concert3Schedule1.id, seatNumber = 1))
        concertSeatJpaRepository.save(ConcertSeatEntity(concertScheduleId = concert4Schedule1.id, seatNumber = 1))
    }

    @Test
    fun `콘서트 당 예약의 개수를 조회할 수 있다`() {
        // given
        val today = LocalDate.now()
        val yesterday = today.minusDays(1)
        reservationJpaRepository.save(createReservation(concertSeatId = 1, createdAt = today)) // concert1
        reservationJpaRepository.save(createReservation(concertSeatId = 2, createdAt = today)) // concert1
        reservationJpaRepository.save(createReservation(concertSeatId = 3, createdAt = today)) // concert2
        reservationJpaRepository.save(createReservation(concertSeatId = 3, createdAt = today)) // concert2
        reservationJpaRepository.save(createReservation(concertSeatId = 3, createdAt = today)) // concert2
        reservationJpaRepository.save(createReservation(concertSeatId = 4, createdAt = today)) // concert3

        // when
        val result = reservationRepositoryImpl.findConcertReservationCountsByDate(today)

        // then
        assertThat(result).hasSize(4)
        assertThat(result.find { it.concertId == 1L }!!.count).isEqualTo(2)
        assertThat(result.find { it.concertId == 2L }!!.count).isEqualTo(3)
        assertThat(result.find { it.concertId == 3L }!!.count).isEqualTo(1)
        assertThat(result.find { it.concertId == 4L }!!.count).isEqualTo(0)
    }

    private fun createConcertScheduleEntity(concertId: Long): ConcertScheduleEntity {
        return Instancio.of(ConcertScheduleEntity::class.java)
            .set(field(ConcertScheduleEntity::id), 0)
            .set(field(ConcertScheduleEntity::concertId), concertId)
            .create()
    }

    private fun createReservation(concertSeatId: Long, createdAt: LocalDate): ReservationEntity {
        return Instancio.of(ReservationEntity::class.java)
            .set(field(ReservationEntity::id), 0)
            .set(field(ReservationEntity::concertSeatId), concertSeatId)
            .set(field(ReservationEntity::expiredAt), LocalDateTime.now().plusMinutes(2))
            .set(field(ReservationEntity::createdAt), createdAt.atTime(LocalTime.of(12, 0)))
            .create()
    }
}