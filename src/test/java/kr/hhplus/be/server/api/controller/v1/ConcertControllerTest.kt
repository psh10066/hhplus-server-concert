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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class ConcertControllerTest : ControllerIntegrationTest() {

    @Test
    fun getSchedules() {
        mockMvc.perform(
            get("/api/v1/concerts/{concertId}/schedules", 1L)
                .header("token", "token:123")
        )
            .andExpect(status().isOk)
            .andDo(
                document(
                    "콘서트 날짜 조회",
                    resource(
                        ResourceSnippetParameters.builder()
                            .tag("콘서트")
                            .summary("콘서트 날짜 조회 API")
                            .description("콘서트 날짜 조회 API")
                            .requestHeaders(
                                headerWithName("token").description("대기열 토큰")
                            )
                            .pathParameters(
                                parameterWithName("concertId").description("콘서트 ID")
                            )
                            .responseSchema(Schema("GetConcertScheduleResponse"))
                            .responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("요청 성공 여부"),
                                fieldWithPath("data.schedules[].concertScheduleId").type(JsonFieldType.NUMBER).description("콘서트 일정 ID"),
                                fieldWithPath("data.schedules[].date").type(JsonFieldType.STRING).description("콘서트 날짜"),
                                fieldWithPath("error").type(JsonFieldType.NULL).ignored(),
                            )
                            .build()
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
                    "콘서트 좌석 조회",
                    resource(
                        ResourceSnippetParameters.builder()
                            .tag("콘서트")
                            .summary("콘서트 좌석 조회 API")
                            .description("콘서트 좌석 조회 API")
                            .requestHeaders(
                                headerWithName("token").description("대기열 토큰")
                            )
                            .pathParameters(
                                parameterWithName("concertScheduleId").description("콘서트 일정 ID")
                            )
                            .responseSchema(Schema("GetConcertSeatResponse"))
                            .responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("요청 성공 여부"),
                                fieldWithPath("data.seats[].concertSeatId").type(JsonFieldType.NUMBER).description("콘서트 좌석 ID"),
                                fieldWithPath("data.seats[].seatNumber").type(JsonFieldType.NUMBER).description("콘서트 좌석 번호"),
                                fieldWithPath("error").type(JsonFieldType.NULL).ignored(),
                            )
                            .build()
                    )
                )
            )
    }
}