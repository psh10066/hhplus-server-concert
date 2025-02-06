package kr.hhplus.be.server.api

import kr.hhplus.be.server.domain.model.queue.Queue
import kr.hhplus.be.server.domain.model.queue.QueueStatus
import kr.hhplus.be.server.helper.CleanUp
import kr.hhplus.be.server.infrastructure.dao.queue.QueueRedisRepository
import kr.hhplus.be.server.infrastructure.dao.user.UserEntity
import kr.hhplus.be.server.infrastructure.dao.user.UserJpaRepository
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper
import java.time.LocalDateTime
import kotlin.time.Duration.Companion.minutes

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
abstract class ControllerIntegrationTest {
    protected val objectMapper = ObjectMapper()

    @Autowired
    protected lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var cleanUp: CleanUp

    @Autowired
    private lateinit var userJpaRepository: UserJpaRepository

    @Autowired
    private lateinit var queueRedisRepository: QueueRedisRepository

    lateinit var activeToken: String

    @BeforeEach
    fun commonSetUp() {
        cleanUp.all()
        val user = userJpaRepository.save(UserEntity(name = "홍길동")) // UserInterceptor 테스트를 위해 유저 생성
        val token = Queue.create(user.uuid).token
        queueRedisRepository.addWaitingToken(token, 0.0)
        queueRedisRepository.activateTokens(1, 5.minutes)
        activeToken = token
    }
}