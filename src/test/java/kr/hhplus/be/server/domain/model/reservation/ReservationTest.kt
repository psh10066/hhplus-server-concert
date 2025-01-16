package kr.hhplus.be.server.domain.model.reservation

import kr.hhplus.be.server.support.error.CustomException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.time.Clock
import java.time.LocalDateTime
import java.time.ZoneOffset

class ReservationTest {

    @Test
    fun `콘서트 예약 시 예약 상태로 생성된다`() {
        // when
        val reservation = Reservation.book(Clock.systemDefaultZone(), 2L, 3L)

        // then
        assertThat(reservation.status).isEqualTo(ReservationStatus.BOOKED)
    }

    @Test
    fun `콘서트 예약 시 만료 시간이 5분 뒤로 설정된다`() {
        // when
        val now = LocalDateTime.now()
        val clock = Clock.fixed(now.toInstant(ZoneOffset.UTC), ZoneOffset.UTC)
        val reservation = Reservation.book(clock, 2L, 3L)

        // then
        assertThat(reservation.expiredAt).isEqualTo(now.plusMinutes(5L))
    }

    @Test
    fun `결제 완료된 경우 예약된 상태로 취급된다`() {
        // given
        val reservation = Reservation(
            concertSeatId = 2L,
            userId = 3L,
            status = ReservationStatus.PAYMENT_COMPLETED
        )

        // when
        val isBooked = reservation.isBooked(Clock.systemDefaultZone())

        // then
        assertThat(isBooked).isTrue()
    }

    @Test
    fun `만료 시간이 지나지 않은 경우 예약된 상태로 취급된다`() {
        // given
        val reservation = Reservation(
            concertSeatId = 2L,
            userId = 3L,
            status = ReservationStatus.BOOKED,
            expiredAt = LocalDateTime.now().plusMinutes(1)
        )

        // when
        val isBooked = reservation.isBooked(Clock.systemDefaultZone())

        // then
        assertThat(isBooked).isTrue()
    }

    @Test
    fun `만료 시간이 지난 경우 예약되지 않은 상태로 취급된다`() {
        // given
        val reservation = Reservation(
            concertSeatId = 2L,
            userId = 3L,
            status = ReservationStatus.BOOKED,
            expiredAt = LocalDateTime.now().minusMinutes(1)
        )

        // when
        val isBooked = reservation.isBooked(Clock.systemDefaultZone())

        // then
        assertThat(isBooked).isFalse()
    }

    @Test
    fun `콘서트 결제 시 이미 결제 완료된 예약인 경우 CustomException이 발생한다`() {
        // given
        val reservation = Reservation(
            concertSeatId = 2L,
            userId = 3L,
            status = ReservationStatus.PAYMENT_COMPLETED
        )

        // when then
        assertThatThrownBy {
            reservation.pay(Clock.systemDefaultZone())
        }
            .isInstanceOf(CustomException::class.java)
            .hasMessage("결제 가능한 예약이 아닙니다.")
    }

    @Test
    fun `콘서트 결제 시 만료 시간이 지난 예약 상태의 경우 CustomException이 발생한다`() {
        // given
        val reservation = Reservation(
            concertSeatId = 2L,
            userId = 3L,
            status = ReservationStatus.BOOKED,
            expiredAt = LocalDateTime.now().minusMinutes(1)
        )

        // when then
        assertThatThrownBy {
            reservation.pay(Clock.systemDefaultZone())
        }
            .isInstanceOf(CustomException::class.java)
            .hasMessage("결제 가능한 예약이 아닙니다.")
    }

    @Test
    fun `콘서트 결제 시 결제 상태로 변경된다`() {
        // given
        val reservation = Reservation(
            concertSeatId = 2L,
            userId = 3L,
            status = ReservationStatus.BOOKED,
            expiredAt = LocalDateTime.now().plusMinutes(1)
        )

        // when
        reservation.pay(Clock.systemDefaultZone())

        // then
        assertThat(reservation.status).isEqualTo(ReservationStatus.PAYMENT_COMPLETED)
    }

    @Test
    fun `콘서트 결제 시 만료 시간이 제거된다`() {
        // given
        val reservation = Reservation(
            concertSeatId = 2L,
            userId = 3L,
            status = ReservationStatus.BOOKED,
            expiredAt = LocalDateTime.now().plusMinutes(1)
        )

        // when
        reservation.pay(Clock.systemDefaultZone())

        // then
        assertThat(reservation.expiredAt).isNull()
    }
}