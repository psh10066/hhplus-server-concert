package kr.hhplus.be.server.domain.service

import kr.hhplus.be.server.domain.model.queue.Queue
import kr.hhplus.be.server.domain.model.queue.QueueRepository
import kr.hhplus.be.server.helper.KSelect.Companion.field
import org.assertj.core.api.Assertions.assertThat
import org.instancio.Instancio
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.given
import java.time.Clock
import java.util.*

class QueueServiceTest {

    private lateinit var queueRepository: QueueRepository
    private lateinit var queueService: QueueService

    @BeforeEach
    fun setUp() {
        queueRepository = mock()
        queueService = QueueService(Clock.systemDefaultZone(), queueRepository)
    }

    @Test
    fun `존재하는 대기열 토큰이 없으면 토큰을 생성하여 반환한다`() {
        // given
        val uuid = UUID.randomUUID()
        given(queueRepository.findByUserUuid(uuid)).willReturn(null)
        val queue = createQueue(uuid)
        given(queueRepository.save(any())).willReturn(queue)

        // when
        val result = queueService.issueToken(uuid)

        // then
        assertThat(result).isEqualTo(queue.token)
    }

    @Test
    fun `존재하는 대기열 토큰이 있으면 해당 토큰을 그대로 반환한다`() {
        // given
        val uuid = UUID.randomUUID()
        val queue = createQueue(uuid)
        given(queueRepository.findByUserUuid(uuid)).willReturn(queue)

        // when
        val result = queueService.issueToken(uuid)

        // then
        assertThat(result).isEqualTo(queue.token)
    }

    private fun createQueue(userUuid: UUID): Queue {
        return Instancio.of(Queue::class.java)
            .set(field(Queue::userUuid), userUuid)
            .create()
    }
}