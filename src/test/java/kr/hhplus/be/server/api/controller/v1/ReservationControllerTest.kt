package kr.hhplus.be.server.api.controller.v1

import kr.hhplus.be.server.api.RestDocsTest
import kr.hhplus.be.server.api.controller.v1.request.ConcertReservationRequest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.operation.preprocess.Preprocessors.*
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class ReservationControllerTest : RestDocsTest() {

    private lateinit var controller: ReservationController

    @BeforeEach
    fun setUp() {
        controller = ReservationController()
        mockMvc = mockController(controller)
    }

    @Test
    fun concertReservation() {
        mockMvc.perform(
            post("/api/v1/reservations/concert")
                .header("token", "token:123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ConcertReservationRequest(concertScheduleId = 1L, concertSeatId = 2L)))
        )
            .andExpect(status().isOk)
            .andDo(
                document(
                    "api/v1/reservations/concert",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestFields(
                        fieldWithPath("concertScheduleId").type(JsonFieldType.NUMBER).description("콘서트 일정 ID"),
                        fieldWithPath("concertSeatId").type(JsonFieldType.NUMBER).description("콘서트 좌석 ID"),
                    ),
                    responseFields(
                        fieldWithPath("result").type(JsonFieldType.STRING).description("요청 성공 여부"),
                        fieldWithPath("data.reservationId").type(JsonFieldType.NUMBER).description("예약 ID"),
                        fieldWithPath("error").type(JsonFieldType.NULL).ignored(),
                    )
                )
            )
    }
}