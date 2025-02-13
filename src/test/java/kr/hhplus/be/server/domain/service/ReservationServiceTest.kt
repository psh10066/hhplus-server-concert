package kr.hhplus.be.server.domain.service

import kr.hhplus.be.server.domain.model.concert.Concert
import kr.hhplus.be.server.domain.model.reservation.Reservation
import kr.hhplus.be.server.domain.model.reservation.ReservationRepository
import kr.hhplus.be.server.domain.model.reservation.ReservationStatus
import kr.hhplus.be.server.domain.model.user.User
import kr.hhplus.be.server.support.client.ConcertApiClient
import kr.hhplus.be.server.support.error.CustomException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.given
import org.springframework.context.ApplicationEventPublisher
import java.time.Clock

class ReservationServiceTest {

    private lateinit var reservationRepository: ReservationRepository
    private lateinit var applicationEventPublisher: ApplicationEventPublisher
    private lateinit var concertApiClient: ConcertApiClient
    private lateinit var reservationService: ReservationService

    @BeforeEach
    fun setUp() {
        reservationRepository = mock()
        applicationEventPublisher = mock()
        concertApiClient = mock()
        reservationService = ReservationService(Clock.systemDefaultZone(), reservationRepository, concertApiClient, applicationEventPublisher)
    }

    @Test
    fun `콘서트 예약 시 해당 좌석에 이미 예약이 있으면 CustomException이 발생한다`() {
        // given
        val reservation1: Reservation = mock()
        val reservation2: Reservation = mock()
        given(reservation1.isReserved(any())).willReturn(false)
        given(reservation1.isReserved(any())).willReturn(true)


        given(reservationRepository.findConcertReservation(any())).willReturn(listOf(reservation1, reservation2))

        // when then
        assertThatThrownBy {
            reservationService.concertReservation(User(id = 1, name = "홍길동"), 3L)
        }
            .isInstanceOf(CustomException::class.java)
            .hasMessage("이미 예약된 좌석입니다.")
    }

    @Test
    fun `콘서트 예약 시 해당 좌석에 예약이 없으면 예약에 성공한다`() {
        // given
        val reservation1: Reservation = mock()
        val reservation2: Reservation = mock()
        given(reservation1.isReserved(any())).willReturn(false)
        given(reservation1.isReserved(any())).willReturn(false)

        given(concertApiClient.getConcertBySeatId(3L)).willReturn(Concert(4L, "아이유 콘서트", 1000000))
        given(reservationRepository.findConcertReservation(any())).willReturn(listOf(reservation1, reservation2))
        given(reservationRepository.save(any())).willReturn(Reservation(id = 2L, concertId = 4L, concertSeatId = 3L, userId = 1L, status = ReservationStatus.RESERVED))

        // when
        val result = reservationService.concertReservation(User(id = 1, name = "홍길동"), 3L)

        // then
        assertThat(result).isEqualTo(2L)
    }
}