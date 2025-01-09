package kr.hhplus.be.server.api.controller.v1

import com.epages.restdocs.apispec.ResourceDocumentation.resource
import com.epages.restdocs.apispec.ResourceSnippetParameters
import com.epages.restdocs.apispec.Schema
import kr.hhplus.be.server.api.ControllerIntegrationTest
import kr.hhplus.be.server.api.controller.v1.request.ConcertReservationRequest
import kr.hhplus.be.server.domain.model.concert.ConcertSchedule
import kr.hhplus.be.server.domain.model.concert.ConcertSeat
import kr.hhplus.be.server.infrastructure.dao.concert.ConcertScheduleJpaRepository
import kr.hhplus.be.server.infrastructure.dao.concert.ConcertSeatJpaRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime

class ReservationControllerTest(
    @Autowired private val concertScheduleJpaRepository: ConcertScheduleJpaRepository,
    @Autowired private val concertSeatJpaRepository: ConcertSeatJpaRepository
) : ControllerIntegrationTest() {

    @BeforeEach
    fun setUp() {
        concertScheduleJpaRepository.save(ConcertSchedule(concertId = 1L, startTime = LocalDateTime.of(2025, 1, 8, 11, 0)))
        concertSeatJpaRepository.save(ConcertSeat(concertId = 1L, seatNumber = 1))
        concertSeatJpaRepository.save(ConcertSeat(concertId = 1L, seatNumber = 2))
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
                    "콘서트 예약",
                    resource(
                        ResourceSnippetParameters.builder()
                            .tag("예약")
                            .summary("콘서트 예약 API")
                            .description("콘서트 좌석 임시 배정 API")
                            .requestHeaders(
                                headerWithName("token").description("대기열 토큰")
                            )
                            .requestSchema(Schema("ConcertReservationRequest"))
                            .requestFields(
                                fieldWithPath("concertScheduleId").type(JsonFieldType.NUMBER).description("콘서트 일정 ID"),
                                fieldWithPath("concertSeatId").type(JsonFieldType.NUMBER).description("콘서트 좌석 ID"),
                            )
                            .responseSchema(Schema("ConcertReservationResponse"))
                            .responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("요청 성공 여부"),
                                fieldWithPath("data.reservationId").type(JsonFieldType.NUMBER).description("예약 ID"),
                                fieldWithPath("error").type(JsonFieldType.NULL).ignored(),
                            )
                            .build()
                    )
                )
            )
    }
}