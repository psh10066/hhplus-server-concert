package kr.hhplus.be.server.domain.service

import kr.hhplus.be.server.domain.model.reservation.Reservation
import kr.hhplus.be.server.domain.model.reservation.ReservationRepository
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
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
    fun `콘서트 예약 시 해당 좌석에 이미 예약이 있으면 IllegalStateException이 발생한다`() {
        // given
        val reservation1: Reservation = mock()
        val reservation2: Reservation = mock()
        given(reservation1.isBooked(any())).willReturn(false)
        given(reservation1.isBooked(any())).willReturn(true)


        given(reservationRepository.findConcertReservation(any(), any())).willReturn(listOf(reservation1, reservation2))

        // when then
        assertThatThrownBy {
            reservationService.concertReservation(1L, 2L, 3L)
        }
            .isInstanceOf(IllegalStateException::class.java)
            .hasMessage("이미 예약된 좌석입니다.")
    }

    @Test
    fun `콘서트 예약 시 해당 좌석에 예약이 없으면 예약에 성공한다`() {
        // given
        val reservation1: Reservation = mock()
        val reservation2: Reservation = mock()
        given(reservation1.isBooked(any())).willReturn(false)
        given(reservation1.isBooked(any())).willReturn(false)

        given(reservationRepository.findConcertReservation(any(), any())).willReturn(listOf(reservation1, reservation2))

        // when
        reservationService.concertReservation(1L, 2L, 3L)

        // then
        verify(reservationRepository).save(any())
    }

    @Test
    fun `콘서트 결제 시 해당 예약에 결제할 수 없으면 IllegalStateException이 발생한다`() {
        // given
        val reservation: Reservation = mock()
        given(reservation.isPayable(any())).willReturn(false)
        given(reservationRepository.getById(any())).willReturn(reservation)

        // when then
        assertThatThrownBy {
            reservationService.payReservation(1L)
        }
            .isInstanceOf(IllegalStateException::class.java)
            .hasMessage("결제 가능한 예약이 아닙니다.")
    }

    @Test
    fun `콘서트 결제 시 해당 예약에 결제할 수 있으면 결제에 성공한다`() {
        // given
        val reservation: Reservation = mock()
        given(reservation.isPayable(any())).willReturn(true)
        given(reservationRepository.getById(any())).willReturn(reservation)

        // when
        reservationService.payReservation(1L)

        // then
        verify(reservationRepository).save(any())
    }
}