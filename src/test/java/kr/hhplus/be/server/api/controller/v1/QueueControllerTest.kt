package kr.hhplus.be.server.api.controller.v1

import kr.hhplus.be.server.api.RestDocsTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.operation.preprocess.Preprocessors.*
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class QueueControllerTest : RestDocsTest() {

    private lateinit var controller: QueueController

    @BeforeEach
    fun setUp() {
        controller = QueueController()
        mockMvc = mockController(controller)
    }

    @Test
    fun issueToken() {
        mockMvc.perform(
            post("/api/v1/queues/token")
                .header("userId", 1L)
        )
            .andExpect(status().isOk)
            .andDo(
                document(
                    "api/v1/queues/token",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("result").type(JsonFieldType.STRING).description("요청 성공 여부"),
                        fieldWithPath("data.token").type(JsonFieldType.STRING).description("대기열 토큰"),
                        fieldWithPath("error").type(JsonFieldType.NULL).ignored(),
                    )
                )
            )
    }
}