package kr.hhplus.be.server.api

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper

@ExtendWith(RestDocumentationExtension::class)
abstract class RestDocsTest {

    protected val objectMapper = ObjectMapper()
    protected lateinit var mockMvc: MockMvc
    private lateinit var restDocumentation: RestDocumentationContextProvider

    @BeforeEach
    fun setUp(restDocumentation: RestDocumentationContextProvider) {
        this.restDocumentation = restDocumentation
    }

    protected fun mockController(controller: Any): MockMvc {
        return MockMvcBuilders.standaloneSetup(controller)
            .apply<StandaloneMockMvcBuilder>(MockMvcRestDocumentation.documentationConfiguration(restDocumentation))
            .build()
    }
}