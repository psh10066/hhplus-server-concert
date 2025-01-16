package kr.hhplus.be.server.infrastructure.dao.concert

import kr.hhplus.be.server.domain.model.reservation.ReservationStatus
import kr.hhplus.be.server.helper.CleanUp
import kr.hhplus.be.server.helper.KSelect.Companion.field
import kr.hhplus.be.server.infrastructure.dao.reservation.ReservationEntity
import kr.hhplus.be.server.infrastructure.dao.reservation.ReservationJpaRepository
import org.assertj.core.api.Assertions.assertThat
import org.instancio.Instancio
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime

@SpringBootTest
class ConcertSeatRepositoryImplTest(
    @Autowired private val cleanUp: CleanUp,
    @Autowired private val concertJpaRepository: ConcertJpaRepository,
    @Autowired private val concertScheduleJpaRepository: ConcertScheduleJpaRepository,
    @Autowired private val concertSeatJpaRepository: ConcertSeatJpaRepository,
    @Autowired private val reservationJpaRepository: ReservationJpaRepository,
    @Autowired private val concertSeatRepositoryImpl: ConcertSeatRepositoryImpl
) {

    @BeforeEach
    fun setUp() {
        cleanUp.all()
    }

    @Test
    fun `예약되지 않은 좌석만 조회할 수 있다`() {
        // given
        val concert = concertJpaRepository.save(createConcertEntity())
        val concertSchedule = concertScheduleJpaRepository.save(createConcertScheduleEntity(concert.id))
        val seat1 = concertSeatJpaRepository.save(ConcertSeatEntity(concertId = concert.id, seatNumber = 1))
        val seat2 = concertSeatJpaRepository.save(ConcertSeatEntity(concertId = concert.id, seatNumber = 2))
        val seat3 = concertSeatJpaRepository.save(ConcertSeatEntity(concertId = concert.id, seatNumber = 3))
        val seat4 = concertSeatJpaRepository.save(ConcertSeatEntity(concertId = concert.id, seatNumber = 3))
        reservationJpaRepository.save(
            ReservationEntity(
                concertScheduleId = concertSchedule.id,
                concertSeatId = seat1.id,
                userId = 1L,
                status = ReservationStatus.PAYMENT_COMPLETED
            )
        )
        reservationJpaRepository.save(
            ReservationEntity(
                concertScheduleId = concertSchedule.id,
                concertSeatId = seat2.id,
                userId = 1L,
                status = ReservationStatus.BOOKED,
                expiredAt = LocalDateTime.now().plusMinutes(3)
            )
        )

        // when
        val result = concertSeatRepositoryImpl.findAvailableSeats(concertSchedule.id)

        // then
        assertThat(result).hasSize(2)
        assertThat(result[0].id).isEqualTo(seat3.id)
        assertThat(result[1].id).isEqualTo(seat4.id)
    }

    @Test
    fun `동일한 자리에 만료된 예약이 여러 개 존재해도 예약 가능 좌석으로 조회할 수 있다`() {
        // given
        val concert = concertJpaRepository.save(createConcertEntity())
        val concertSchedule = concertScheduleJpaRepository.save(createConcertScheduleEntity(concert.id))
        val seat1 = concertSeatJpaRepository.save(ConcertSeatEntity(concertId = concert.id, seatNumber = 1))
        val seat2 = concertSeatJpaRepository.save(ConcertSeatEntity(concertId = concert.id, seatNumber = 2))
        val seat3 = concertSeatJpaRepository.save(ConcertSeatEntity(concertId = concert.id, seatNumber = 3))
        reservationJpaRepository.save(
            ReservationEntity(
                concertScheduleId = concertSchedule.id,
                concertSeatId = seat1.id,
                userId = 1L,
                status = ReservationStatus.BOOKED,
                expiredAt = LocalDateTime.now().minusMinutes(3)
            )
        )
        reservationJpaRepository.save(
            ReservationEntity(
                concertScheduleId = concertSchedule.id,
                concertSeatId = seat1.id,
                userId = 2L,
                status = ReservationStatus.BOOKED,
                expiredAt = LocalDateTime.now().minusMinutes(10)
            )
        )

        // when
        val result = concertSeatRepositoryImpl.findAvailableSeats(concertSchedule.id)

        // then
        assertThat(result).hasSize(3)
        assertThat(result[0].id).isEqualTo(seat1.id)
        assertThat(result[1].id).isEqualTo(seat2.id)
        assertThat(result[2].id).isEqualTo(seat3.id)
    }

    @Test
    fun `해당 콘서트의 좌석만 조회할 수 있다`() {
        // given
        val concert = concertJpaRepository.save(createConcertEntity())
        val concertSchedule = concertScheduleJpaRepository.save(createConcertScheduleEntity(concert.id))
        val seat1 = concertSeatJpaRepository.save(ConcertSeatEntity(concertId = concert.id, seatNumber = 1))
        val seat2 = concertSeatJpaRepository.save(ConcertSeatEntity(concertId = concert.id, seatNumber = 2))
        concertSeatJpaRepository.save(ConcertSeatEntity(concertId = concert.id + 1, seatNumber = 1))
        concertSeatJpaRepository.save(ConcertSeatEntity(concertId = concert.id + 1, seatNumber = 2))

        // when
        val result = concertSeatRepositoryImpl.findAvailableSeats(concertSchedule.id)

        // then
        assertThat(result).hasSize(2)
        assertThat(result[0].id).isEqualTo(seat1.id)
        assertThat(result[1].id).isEqualTo(seat2.id)
    }

    private fun createConcertEntity(): ConcertEntity {
        return Instancio.of(ConcertEntity::class.java)
            .set(field(ConcertEntity::id), 0)
            .create()
    }

    private fun createConcertScheduleEntity(concertId: Long): ConcertScheduleEntity {
        return Instancio.of(ConcertScheduleEntity::class.java)
            .set(field(ConcertScheduleEntity::id), 0)
            .set(field(ConcertScheduleEntity::concertId), concertId)
            .create()
    }
}