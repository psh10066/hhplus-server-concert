package kr.hhplus.be.server.infrastructure.dao.queue

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.StringRedisTemplate
import java.util.*
import kotlin.time.Duration.Companion.minutes

@SpringBootTest
class QueueRedisRepositoryTest(
    @Autowired private val redisTemplate: StringRedisTemplate,
    @Autowired private val queueRedisRepository: QueueRedisRepository
) {

    @Test
    fun `대기 토큰 저장소에서 원하는 개수만큼 활성 토큰 저장소로 옮길 수 있다`() {
        // given
        val userUuid1 = UUID.randomUUID()
        val userUuid2 = UUID.randomUUID()
        val userUuid3 = UUID.randomUUID()
        val userUuid4 = UUID.randomUUID()
        redisTemplate.opsForZSet().add(WAITING_TOKENS_KEY, "$userUuid2|token2", 2.0)
        redisTemplate.opsForZSet().add(WAITING_TOKENS_KEY, "$userUuid4|token4", 4.0)
        redisTemplate.opsForZSet().add(WAITING_TOKENS_KEY, "$userUuid3|token3", 3.0)
        redisTemplate.opsForZSet().add(WAITING_TOKENS_KEY, "$userUuid1|token1", 1.0)

        // when
        queueRedisRepository.activateTokens(2, 5.minutes)

        // then
        assertThat(redisTemplate.opsForZSet().size(WAITING_TOKENS_KEY)).isEqualTo(2)
        assertThat(redisTemplate.opsForValue().get(ACTIVE_TOKEN_PREFIX + userUuid1)).isEqualTo("$userUuid1|token1")
        assertThat(redisTemplate.opsForValue().get(ACTIVE_TOKEN_PREFIX + userUuid2)).isEqualTo("$userUuid2|token2")
        assertThat(redisTemplate.opsForValue().get(ACTIVE_TOKEN_PREFIX + userUuid3)).isNull()
        assertThat(redisTemplate.opsForValue().get(ACTIVE_TOKEN_PREFIX + userUuid4)).isNull()
    }
}