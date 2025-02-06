package kr.hhplus.be.server.domain.service

import kr.hhplus.be.server.domain.model.queue.QueueRank
import kr.hhplus.be.server.domain.model.queue.QueueRepository
import kr.hhplus.be.server.domain.model.queue.QueueStatus
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.given
import java.time.Clock

class QueueServiceTest {

    private lateinit var queueRepository: QueueRepository
    private lateinit var queueService: QueueService

    @BeforeEach
    fun setUp() {
        queueRepository = mock()
        queueService = QueueService(Clock.systemDefaultZone(), queueRepository)
    }

    @Test
    fun `토큰 순위 조회시 대기 토큰이면 순위와 함께 상태를 반환한다`() {
        // given
        given(queueRepository.findWaitingTokenRank("token")).willReturn(1)

        // when
        val result = queueService.getQueueRank("token")

        // then
        assertThat(result.status).isEqualTo(QueueStatus.WAITING)
        assertThat(result.rank).isEqualTo(1)
    }

    @Test
    fun `토큰 순위 조회시 활성 토큰이면 상태만 반환한다`() {
        // given
        given(queueRepository.findWaitingTokenRank("token")).willReturn(null)
        given(queueRepository.existActiveToken("token")).willReturn(true)

        // when
        val result = queueService.getQueueRank("token")

        // then
        assertThat(result.status).isEqualTo(QueueStatus.ACTIVE)
        assertThat(result.rank).isNull()
    }
}