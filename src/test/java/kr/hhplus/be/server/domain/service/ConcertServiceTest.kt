package kr.hhplus.be.server.domain.service

import kr.hhplus.be.server.domain.model.common.dto.PageDto
import kr.hhplus.be.server.domain.model.concert.*
import org.assertj.core.api.Assertions.assertThat
import org.instancio.Instancio
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.given

class ConcertServiceTest {

    private lateinit var concertRepository: ConcertRepository
    private lateinit var concertScheduleRepository: ConcertScheduleRepository
    private lateinit var concertSeatRepository: ConcertSeatRepository
    private lateinit var concertService: ConcertService

    @BeforeEach
    fun setUp() {
        concertRepository = mock()
        concertScheduleRepository = mock()
        concertSeatRepository = mock()
        concertService = ConcertService(concertRepository, concertScheduleRepository, concertSeatRepository)
    }

    @Test
    fun `페이지 정보를 조회할 수 있다`() {
        // given
        val pageDto = PageDto(
            totalElements = 4,
            totalPages = 2,
            content = listOf(
                createConcert(),
                createConcert(),
            )
        )
        given(concertRepository.findPage(1, 2)).willReturn(pageDto)

        // when
        val result = concertService.findPage(1, 2)

        // then
        assertThat(result.totalElements).isEqualTo(pageDto.totalElements)
        assertThat(result.totalPages).isEqualTo(pageDto.totalPages)
        assertThat(result.content).hasSize(pageDto.content.size)
    }

    private fun createConcert(): Concert {
        return Instancio.of(Concert::class.java)
            .create()
    }

    @Test
    fun `콘서트 ID로 콘서트 스케줄 정보를 조회할 수 있다`() {
        // given
        val concertSchedules = listOf(
            createConcertSchedule(),
            createConcertSchedule()
        )
        given(concertScheduleRepository.findAvailablesByConcertId(1L)).willReturn(concertSchedules)

        // when
        val result = concertService.findConcertSchedules(1L)

        // then
        assertThat(result).hasSize(2)
        assertThat(result[0].id).isEqualTo(concertSchedules[0].id)
        assertThat(result[0].concertId).isEqualTo(concertSchedules[0].concertId)
        assertThat(result[0].startTime).isEqualTo(concertSchedules[0].startTime)
        assertThat(result[1].id).isEqualTo(concertSchedules[1].id)
        assertThat(result[1].concertId).isEqualTo(concertSchedules[1].concertId)
        assertThat(result[1].startTime).isEqualTo(concertSchedules[1].startTime)
    }

    private fun createConcertSchedule(): ConcertSchedule {
        return Instancio.of(ConcertSchedule::class.java).create()
    }

    @Test
    fun `콘서트 스케줄 ID로 예약 가능 좌석 정보를 조회할 수 있다`() {
        // given
        val concertSeats = listOf(
            createConcertSeat(),
            createConcertSeat()
        )
        given(concertSeatRepository.findAvailableSeats(1L)).willReturn(concertSeats)

        // when
        val result = concertService.findAvailableSeats(1L)

        // then
        assertThat(result).hasSize(2)
        assertThat(result[0].id).isEqualTo(concertSeats[0].id)
        assertThat(result[0].concertScheduleId).isEqualTo(concertSeats[0].concertScheduleId)
        assertThat(result[0].seatNumber).isEqualTo(concertSeats[0].seatNumber)
        assertThat(result[1].id).isEqualTo(concertSeats[1].id)
        assertThat(result[1].concertScheduleId).isEqualTo(concertSeats[1].concertScheduleId)
        assertThat(result[1].seatNumber).isEqualTo(concertSeats[1].seatNumber)
    }

    private fun createConcertSeat(): ConcertSeat {
        return Instancio.of(ConcertSeat::class.java).create()
    }
}