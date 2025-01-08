package kr.hhplus.be.server.domain.service

import kr.hhplus.be.server.domain.model.queue.Queue
import kr.hhplus.be.server.domain.model.queue.QueueRepository
import kr.hhplus.be.server.domain.model.queue.QueueStatus
import kr.hhplus.be.server.helper.KSelect.Companion.field
import org.assertj.core.api.Assertions.assertThat
import org.instancio.Instancio
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.given
import org.mockito.kotlin.times
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

    @Test
    fun `토큰 갱신 시 비활성화된 토큰만 갱신할 수 있다`() {
        // given
        val queues = listOf(
            createQueueWithStatus(QueueStatus.ACTIVE),
            createQueueWithStatus(QueueStatus.ACTIVE),
            createQueueWithStatus(QueueStatus.ACTIVE),
            createQueueWithStatus(QueueStatus.WAITING),
            createQueueWithStatus(QueueStatus.WAITING),
        )
        given(queueRepository.getNotExpiredWithOrder(any())).willReturn(queues)

        // when
        queueService.activate()

        // then
        verify(queueRepository, times(2)).save(any())
        assertThat(queues.count { it.status == QueueStatus.WAITING }).isEqualTo(0)
    }

    private fun createQueue(userUuid: UUID): Queue {
        return Instancio.of(Queue::class.java)
            .set(field(Queue::userUuid), userUuid)
            .create()
    }

    private fun createQueueWithStatus(status: QueueStatus): Queue {
        return Instancio.of(Queue::class.java)
            .set(field(Queue::status), status)
            .create()
    }
}