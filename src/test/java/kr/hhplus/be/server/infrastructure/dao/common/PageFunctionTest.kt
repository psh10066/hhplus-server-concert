package kr.hhplus.be.server.infrastructure.dao.common

import kr.hhplus.be.server.domain.model.concert.Concert
import org.assertj.core.api.Assertions.assertThat
import org.instancio.Instancio
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.given
import org.springframework.data.domain.Page

class PageFunctionTest {

    @Test
    fun `Spring Data의 Page에서 Domain DTO인 PageDto로 변환할 수 있다`() {
        // given
        val concert = createConcert()
        val page: Page<Concert> = mock()
        given(page.totalElements).willReturn(3)
        given(page.totalPages).willReturn(2)
        given(page.content).willReturn(listOf(concert))

        // when
        val result = page.toDto()

        // then
        assertThat(result.totalElements).isEqualTo(3)
        assertThat(result.totalPages).isEqualTo(2)
        assertThat(result.content).hasSize(1)
        assertThat(result.content[0]).isEqualTo(concert)
    }

    @Test
    fun `1페이지부터 조회할 수 있도록 pageNumber를 1 감소시킨 Pageable을 생성할 수 있다`() {
        // when
        val result = getOneBasedPageable(1, 2)

        // then
        assertThat(result.pageNumber).isEqualTo(0)
        assertThat(result.pageSize).isEqualTo(2)
    }

    private fun createConcert(): Concert {
        return Instancio.of(Concert::class.java)
            .create()
    }
}