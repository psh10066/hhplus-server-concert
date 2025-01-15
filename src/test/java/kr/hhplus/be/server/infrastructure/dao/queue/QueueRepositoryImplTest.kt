package kr.hhplus.be.server.infrastructure.dao.queue

import kr.hhplus.be.server.domain.model.queue.QueueStatus
import kr.hhplus.be.server.helper.CleanUp
import kr.hhplus.be.server.helper.KSelect.Companion.field
import org.assertj.core.api.Assertions.assertThat
import org.instancio.Instancio
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime

@SpringBootTest
class QueueRepositoryImplTest(
    @Autowired private val cleanUp: CleanUp,
    @Autowired private val queueJpaRepository: QueueJpaRepository,
    @Autowired private val queueRepositoryImpl: QueueRepositoryImpl
) {
    @BeforeEach
    fun setUp() {
        cleanUp.all()
    }

    @Test
    fun `만료되지 않은 토큰 중 원하는 개수만 ID 순서대로 조회할 수 있다`() {
        // given
        queueJpaRepository.save(createExpiredQueueEntity(status = QueueStatus.WAITING))
        queueJpaRepository.save(createExpiredQueueEntity(status = QueueStatus.ACTIVE))
        val first = queueJpaRepository.save(createNotExpiredQueueEntity(status = QueueStatus.ACTIVE))
        val second = queueJpaRepository.save(createNotExpiredQueueEntity(status = QueueStatus.WAITING))
        val third = queueJpaRepository.save(createNotExpiredQueueEntity(status = QueueStatus.ACTIVE))
        queueJpaRepository.save(createNotExpiredQueueEntity(status = QueueStatus.WAITING))
        queueJpaRepository.save(createNotExpiredQueueEntity(status = QueueStatus.ACTIVE))

        // when
        val result = queueRepositoryImpl.getNotExpiredWithOrder(3)

        // then
        assertThat(result.size).isEqualTo(3)
        assertThat(result[0].id).isEqualTo(first.id)
        assertThat(result[1].id).isEqualTo(second.id)
        assertThat(result[2].id).isEqualTo(third.id)
    }

    private fun createExpiredQueueEntity(status: QueueStatus): QueueEntity {
        return Instancio.of(QueueEntity::class.java)
            .set(field(QueueEntity::id), 0)
            .set(field(QueueEntity::status), status)
            .set(field(QueueEntity::expiredAt), LocalDateTime.now().minusSeconds(10))
            .create()
    }

    private fun createNotExpiredQueueEntity(status: QueueStatus): QueueEntity {
        return Instancio.of(QueueEntity::class.java)
            .set(field(QueueEntity::id), 0)
            .set(field(QueueEntity::status), status)
            .set(field(QueueEntity::expiredAt), LocalDateTime.now().plusMinutes(10))
            .create()
    }
}