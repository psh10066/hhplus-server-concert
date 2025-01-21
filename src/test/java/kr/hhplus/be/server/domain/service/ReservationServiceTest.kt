package kr.hhplus.be.server.domain.service

import kr.hhplus.be.server.domain.model.reservation.Reservation
import kr.hhplus.be.server.domain.model.reservation.ReservationRepository
import kr.hhplus.be.server.domain.model.reservation.ReservationStatus
import kr.hhplus.be.server.support.error.CustomException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.given
import java.time.Clock

class ReservationServiceTest {

    private lateinit var reservationRepository: ReservationRepository
    private lateinit var reservationService: ReservationService

    @BeforeEach
    fun setUp() {
        reservationRepository = mock()
        reservationService = ReservationService(Clock.systemDefaultZone(), reservationRepository)
    }

    @Test
    fun `콘서트 예약 시 해당 좌석에 이미 예약이 있으면 CustomException이 발생한다`() {
        // given
        val reservation1: Reservation = mock()
        val reservation2: Reservation = mock()
        given(reservation1.isBooked(any())).willReturn(false)
        given(reservation1.isBooked(any())).willReturn(true)


        given(reservationRepository.findConcertReservation(any())).willReturn(listOf(reservation1, reservation2))

        // when then
        assertThatThrownBy {
            reservationService.concertReservation(1L, 3L)
        }
            .isInstanceOf(CustomException::class.java)
            .hasMessage("이미 예약된 좌석입니다.")
    }

    @Test
    fun `콘서트 예약 시 해당 좌석에 예약이 없으면 예약에 성공한다`() {
        // given
        val reservation1: Reservation = mock()
        val reservation2: Reservation = mock()
        given(reservation1.isBooked(any())).willReturn(false)
        given(reservation1.isBooked(any())).willReturn(false)

        given(reservationRepository.findConcertReservation(any())).willReturn(listOf(reservation1, reservation2))
        given(reservationRepository.save(any())).willReturn(Reservation(id = 2L, concertSeatId = 3L, userId = 1L, status = ReservationStatus.BOOKED))

        // when
        val result = reservationService.concertReservation(1L, 3L)

        // then
        assertThat(result).isEqualTo(2L)
    }
}