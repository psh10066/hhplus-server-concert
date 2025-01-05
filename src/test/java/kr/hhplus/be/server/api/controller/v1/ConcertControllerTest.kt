package kr.hhplus.be.server.api.controller.v1

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document
import kr.hhplus.be.server.api.RestDocsTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders
import org.springframework.restdocs.operation.preprocess.Preprocessors.*
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class ConcertControllerTest : RestDocsTest() {

    private lateinit var controller: ConcertController

    @BeforeEach
    fun setUp() {
        controller = ConcertController()
        mockMvc = mockController(controller)
    }

    @Test
    fun getSchedules() {
        mockMvc.perform(
            get("/api/v1/concerts/{concertId}/schedules", 1L)
                .header("token", "token:123")
        )
            .andExpect(status().isOk)
            .andDo(
                document(
                    "api/v1/concerts/schedules",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestHeaders(
                        headerWithName("token").description("대기열 토큰")
                    ),
                    pathParameters(
                        parameterWithName("concertId").description("콘서트 ID")
                    ),
                    responseFields(
                        fieldWithPath("result").type(JsonFieldType.STRING).description("요청 성공 여부"),
                        fieldWithPath("data.schedules[].concertScheduleId").type(JsonFieldType.NUMBER).description("콘서트 일정 ID"),
                        fieldWithPath("data.schedules[].date").type(JsonFieldType.STRING).description("콘서트 날짜"),
                        fieldWithPath("error").type(JsonFieldType.NULL).ignored(),
                    )
                )
            )
    }

    @Test
    fun getSeats() {
        mockMvc.perform(
            get("/api/v1/concerts/schedules/{concertScheduleId}/seats", 1L)
                .header("token", "token:123")
        )
            .andExpect(status().isOk)
            .andDo(
                document(
                    "api/v1/concerts/schedules/seats",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestHeaders(
                        headerWithName("token").description("대기열 토큰")
                    ),
                    pathParameters(
                        parameterWithName("concertScheduleId").description("콘서트 일정 ID")
                    ),
                    responseFields(
                        fieldWithPath("result").type(JsonFieldType.STRING).description("요청 성공 여부"),
                        fieldWithPath("data.seats[].concertSeatId").type(JsonFieldType.NUMBER).description("콘서트 좌석 ID"),
                        fieldWithPath("data.seats[].seatNumber").type(JsonFieldType.NUMBER).description("콘서트 좌석 번호"),
                        fieldWithPath("error").type(JsonFieldType.NULL).ignored(),
                    )
                )
            )
    }
}