package kr.hhplus.be.server.api

import kr.hhplus.be.server.domain.model.user.User
import kr.hhplus.be.server.helper.CleanUp
import kr.hhplus.be.server.infrastructure.dao.user.UserJpaRepository
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
abstract class RestDocsTest {
    protected val objectMapper = ObjectMapper()

    @Autowired
    protected lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var cleanUp: CleanUp

    @Autowired
    private lateinit var userJpaRepository: UserJpaRepository

    @BeforeEach
    fun commonSetUp() {
        cleanUp.all()
        userJpaRepository.save(User(name = "홍길동")) // UserInfoArgumentResolver 테스트를 위해 유저 생성
    }
}