package kr.hhplus.be.server.domain.model.concert

import kr.hhplus.be.server.support.error.CustomException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class ConcertSeatTest {

    @Test
    fun `좌석 예약 시 예약 가능한 좌석의 경우 예약 상태로 변경된다`() {
        // given
        val concertSeat = ConcertSeat(concertScheduleId = 1L, seatNumber = 2, status = ConcertSeatStatus.AVAILABLE)

        // when
        concertSeat.reserve()

        // then
        assertThat(concertSeat.status).isEqualTo(ConcertSeatStatus.RESERVED)
    }

    @Test
    fun `좌석 예약 시 이미 예약된 좌석의 경우 CustomException이 발생한다`() {
        // given
        val concertSeat = ConcertSeat(concertScheduleId = 1L, seatNumber = 2, status = ConcertSeatStatus.RESERVED)

        // when then
        assertThatThrownBy {
            concertSeat.reserve()
        }
            .isInstanceOf(CustomException::class.java)
            .hasMessage("이미 예약된 좌석입니다.")
    }

    @Test
    fun `좌석 결제 시 예약된 좌석 경우 결제 상태로 변경된다`() {
        // given
        val concertSeat = ConcertSeat(concertScheduleId = 1L, seatNumber = 2, status = ConcertSeatStatus.RESERVED)

        // when
        concertSeat.completePayment()

        // then
        assertThat(concertSeat.status).isEqualTo(ConcertSeatStatus.PAYMENT_COMPLETED)
    }

    @Test
    fun `좌석 결제 시 예약된 좌석이 아닌 경우 CustomException이 발생한다`() {
        // given
        val concertSeat = ConcertSeat(concertScheduleId = 1L, seatNumber = 2, status = ConcertSeatStatus.AVAILABLE)

        // when then
        assertThatThrownBy {
            concertSeat.completePayment()
        }
            .isInstanceOf(CustomException::class.java)
            .hasMessage("결제 가능한 예약이 아닙니다.")
    }
}