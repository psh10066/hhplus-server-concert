package kr.hhplus.be.server.api

import kr.hhplus.be.server.domain.model.queue.QueueStatus
import kr.hhplus.be.server.helper.CleanUp
import kr.hhplus.be.server.infrastructure.dao.queue.QueueEntity
import kr.hhplus.be.server.infrastructure.dao.queue.QueueJpaRepository
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
        val user = userJpaRepository.save(UserEntity(name = "홍길동")) // UserArgumentResolver 테스트를 위해 유저 생성
        queueJpaRepository.save(
            QueueEntity(
                userUuid = user.uuid,
                status = QueueStatus.ACTIVE,
                token = "token:123",
                expiredAt = LocalDateTime.now().plusMinutes(10)
            )
        ) // QueueArgumentResolver 테스트를 위해 토큰 생성
    }
}