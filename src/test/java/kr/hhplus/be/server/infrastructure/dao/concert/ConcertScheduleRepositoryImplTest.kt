package kr.hhplus.be.server.infrastructure.dao.concert

import kr.hhplus.be.server.helper.CleanUp
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime

@SpringBootTest
class ConcertScheduleRepositoryImplTest(
    @Autowired private val cleanUp: CleanUp,
    @Autowired private val concertScheduleJpaRepository: ConcertScheduleJpaRepository,
    @Autowired private val concertScheduleRepositoryImpl: ConcertScheduleRepositoryImpl
) {

    @BeforeEach
    fun setUp() {
        cleanUp.all()
    }

    @Test
    fun `현재 시각 이후의 콘서트 스케줄만 조회할 수 있다`() {
        // given
        val now = LocalDateTime.now()

        concertScheduleJpaRepository.save(ConcertScheduleEntity(concertId = 1L, startTime = now.minusMinutes(2)))
        concertScheduleJpaRepository.save(ConcertScheduleEntity(concertId = 1L, startTime = now.minusMinutes(1)))
        val after1 = concertScheduleJpaRepository.save(ConcertScheduleEntity(concertId = 1L, startTime = now.plusMinutes(1)))
        val after2 = concertScheduleJpaRepository.save(ConcertScheduleEntity(concertId = 1L, startTime = now.plusMinutes(2)))

        // when
        val result = concertScheduleRepositoryImpl.findAvailablesByConcertId(1L)
        
        // then
        assertThat(result).hasSize(2)
        assertThat(result[0].id).isEqualTo(after1.id)
        assertThat(result[1].id).isEqualTo(after2.id)
    }
}