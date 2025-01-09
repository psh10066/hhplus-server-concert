package kr.hhplus.be.server.infrastructure.dao.concert

import kr.hhplus.be.server.domain.model.concert.Concert
import kr.hhplus.be.server.helper.CleanUp
import kr.hhplus.be.server.helper.KSelect.Companion.field
import org.assertj.core.api.Assertions.assertThat
import org.instancio.Instancio
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ConcertRepositoryImplTest(
    @Autowired private val cleanUp: CleanUp,
    @Autowired private val concertJpaRepository: ConcertJpaRepository,
    @Autowired private val concertRepositoryImpl: ConcertRepositoryImpl
) {

    @BeforeEach
    fun setUp() {
        cleanUp.all()
    }

    @Test
    fun `1페이지부터 개수에 맞게 페이징 정보를 조회할 수 있다`() {
        // given
        val first = concertJpaRepository.save(createConcert())
        val second = concertJpaRepository.save(createConcert())
        concertJpaRepository.save(createConcert())
        concertJpaRepository.save(createConcert())
        concertJpaRepository.save(createConcert())

        // when
        val result = concertRepositoryImpl.findPage(1, 2)

        // then
        assertThat(result.totalPages).isEqualTo(3)
        assertThat(result.totalElements).isEqualTo(5)
        assertThat(result.content).hasSize(2)
        assertThat(result.content[0]).isEqualTo(first)
        assertThat(result.content[1]).isEqualTo(second)
    }

    private fun createConcert(): Concert {
        return Instancio.of(Concert::class.java)
            .set(field(Concert::id), 0)
            .create()
    }
}