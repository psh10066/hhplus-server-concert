package kr.hhplus.be.server.domain.service

import kr.hhplus.be.server.domain.model.common.dto.PageDto
import kr.hhplus.be.server.domain.model.concert.Concert
import kr.hhplus.be.server.domain.model.concert.ConcertRepository
import org.assertj.core.api.Assertions.assertThat
import org.instancio.Instancio
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.given

class ConcertServiceTest {

    private lateinit var concertRepository: ConcertRepository
    private lateinit var concertService: ConcertService

    @BeforeEach
    fun setUp() {
        concertRepository = mock()
        concertService = ConcertService(concertRepository)
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
}