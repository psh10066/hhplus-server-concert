package kr.hhplus.be.server.api.controller.v1

import com.epages.restdocs.apispec.ResourceDocumentation.resource
import com.epages.restdocs.apispec.ResourceSnippetParameters
import com.epages.restdocs.apispec.Schema
import kr.hhplus.be.server.api.ControllerIntegrationTest
import org.junit.jupiter.api.Test
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class QueueControllerTest : ControllerIntegrationTest() {

    @Test
    fun issueToken() {
        mockMvc.perform(
            post("/api/v1/queues/token")
                .header("userId", 1L)
        )
            .andExpect(status().isOk)
            .andDo(
                document(
                    "대기열 토큰 발급",
                    resource(
                        ResourceSnippetParameters.builder()
                            .tag("대기열")
                            .summary("대기열 토큰 발급 API")
                            .description("유저 대기열 토큰 발급 API")
                            .requestHeaders(
                                headerWithName("userId").description("유저 ID")
                            )
                            .responseSchema(Schema("IssueQueueResponse"))
                            .responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("요청 성공 여부"),
                                fieldWithPath("data.token").type(JsonFieldType.STRING).description("대기열 토큰"),
                                fieldWithPath("error").type(JsonFieldType.NULL).ignored(),
                            )
                            .build()
                    )
                )
            )
    }

    @Test
    fun getQueueRank() {
        mockMvc.perform(
            get("/api/v1/queues/token/rank")
                .header("userId", 1L)
                .header("token", activeToken)
        )
            .andExpect(status().isOk)
            .andDo(
                document(
                    "대기열 토큰 대기 순위 조회",
                    resource(
                        ResourceSnippetParameters.builder()
                            .tag("콘서트")
                            .summary("대기열 토큰 대기 순위 조회 API")
                            .description("대기열 토큰 대기 순위 조회 API")
                            .requestHeaders(
                                headerWithName("userId").description("유저 ID"),
                                headerWithName("token").description("대기열 토큰")
                            )
                            .responseSchema(Schema("GetQueueRankResponse"))
                            .responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("요청 성공 여부"),
                                fieldWithPath("data.status").type(JsonFieldType.STRING).description("대기열 토큰 상태 (WAITING: 대기, ACTIVE: 활성화)"),
                                fieldWithPath("data.rank").type(JsonFieldType.NUMBER).optional().description("대기 상태일 때 대기 순위"),
                                fieldWithPath("error").type(JsonFieldType.NULL).ignored(),
                            )
                            .build()
                    )
                )
            )
    }
}