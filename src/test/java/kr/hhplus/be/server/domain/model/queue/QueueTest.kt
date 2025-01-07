package kr.hhplus.be.server.domain.model.queue

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.Clock
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

class QueueTest {

    @Test
    fun `대기열 생성 시 대기 상태로 생성한다`() {
        // when
        val queue = Queue.create(Clock.systemDefaultZone(), UUID.randomUUID())

        // then
        assertThat(queue.status).isEqualTo(QueueStatus.WAITING)
    }

    @Test
    fun `대기열 생성 시 10분 후 만료되도록 생성한다`() {
        // given
        val now = LocalDateTime.now()
        val clock = Clock.fixed(now.toInstant(ZoneOffset.UTC), ZoneOffset.UTC)

        // when
        val queue = Queue.create(clock, UUID.randomUUID())

        // then
        assertThat(queue.expiredAt).isEqualTo(now.plusMinutes(10L))
    }

    @Test
    fun `대기열 생성 시 토큰을 생성한다`() {
        // when
        val queue = Queue.create(Clock.systemDefaultZone(), UUID.randomUUID())

        // then
        assertThat(queue.token).isEqualTo("${queue.userUuid}|${queue.status}|${queue.expiredAt}")
    }
}