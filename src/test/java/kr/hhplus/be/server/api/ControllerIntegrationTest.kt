package kr.hhplus.be.server.api

import kr.hhplus.be.server.domain.model.queue.Queue
import kr.hhplus.be.server.domain.model.queue.QueueStatus
import kr.hhplus.be.server.domain.model.user.User
import kr.hhplus.be.server.helper.CleanUp
import kr.hhplus.be.server.infrastructure.dao.queue.QueueJpaRepository
import kr.hhplus.be.server.infrastructure.dao.user.UserJpaRepository
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper
import java.time.LocalDateTime
import java.util.*

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
    private lateinit var queueJpaRepository: QueueJpaRepository

    @BeforeEach
    fun commonSetUp() {
        cleanUp.all()
        val user = userJpaRepository.save(User(name = "홍길동")) // UserInfoArgumentResolver 테스트를 위해 유저 생성
        queueJpaRepository.save(
            Queue(
                userUuid = user.uuid,
                status = QueueStatus.ACTIVE,
                token = "token:123",
                expiredAt = LocalDateTime.now().plusMinutes(10)
            )
        ) // QueueInfoArgumentResolver 테스트를 위해 토큰 생성
    }
}