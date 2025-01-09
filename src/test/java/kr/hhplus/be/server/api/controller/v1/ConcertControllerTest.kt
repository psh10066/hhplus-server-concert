package kr.hhplus.be.server.api.controller.v1

import com.epages.restdocs.apispec.ResourceDocumentation.resource
import com.epages.restdocs.apispec.ResourceSnippetParameters
import com.epages.restdocs.apispec.Schema
import kr.hhplus.be.server.api.ControllerIntegrationTest
import kr.hhplus.be.server.domain.model.concert.Concert
import kr.hhplus.be.server.domain.model.concert.ConcertSchedule
import kr.hhplus.be.server.domain.model.concert.ConcertSeat
import kr.hhplus.be.server.infrastructure.dao.concert.ConcertJpaRepository
import kr.hhplus.be.server.infrastructure.dao.concert.ConcertScheduleJpaRepository
import kr.hhplus.be.server.infrastructure.dao.concert.ConcertSeatJpaRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime

class ConcertControllerTest(
    @Autowired private val concertJpaRepository: ConcertJpaRepository,
    @Autowired private val concertScheduleJpaRepository: ConcertScheduleJpaRepository,
    @Autowired private val concertSeatJpaRepository: ConcertSeatJpaRepository
) : ControllerIntegrationTest() {

    @BeforeEach
    fun setUp() {
        concertJpaRepository.save(Concert(name = "아이유 콘서트", 150000L))
        concertJpaRepository.save(Concert(name = "임영웅 콘서트", 130000L))
        concertScheduleJpaRepository.save(ConcertSchedule(concertId = 1L, startTime = LocalDateTime.of(2025, 1, 8, 11, 0)))
        concertScheduleJpaRepository.save(ConcertSchedule(concertId = 1L, startTime = LocalDateTime.of(2025, 1, 9, 12, 0)))
        concertSeatJpaRepository.save(ConcertSeat(concertId = 1L, seatNumber = 1))
        concertSeatJpaRepository.save(ConcertSeat(concertId = 1L, seatNumber = 2))
    }

    @Test
    fun getConcerts() {
        mockMvc.perform(
            get("/api/v1/concerts")
                .header("token", "token:123")
                .param("page", "1")
                .param("size", "10")
        )
            .andExpect(status().isOk)
            .andDo(
                document(
                    "콘서트 목록 조회",
                    resource(
                        ResourceSnippetParameters.builder()
                            .tag("콘서트")
                            .summary("콘서트 목록 조회 API")
                            .description("콘서트 목록 조회 API")
                            .requestHeaders(
                                headerWithName("token").description("대기열 토큰")
                            )
                            .queryParameters(
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("size").description("페이지 크기")
                            )
                            .responseSchema(Schema("GetConcertResponse"))
                            .responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("요청 성공 여부"),
                                fieldWithPath("data.concerts.totalElements").type(JsonFieldType.NUMBER).description("총 개수"),
                                fieldWithPath("data.concerts.totalPages").type(JsonFieldType.NUMBER).description("총 페이지 수"),
                                fieldWithPath("data.concerts.content[].id").type(JsonFieldType.NUMBER).description("콘서트 ID"),
                                fieldWithPath("data.concerts.content[].name").type(JsonFieldType.STRING).description("콘서트 명"),
                                fieldWithPath("data.concerts.content[].price").type(JsonFieldType.NUMBER).description("콘서트 가격"),
                                fieldWithPath("error").type(JsonFieldType.NULL).ignored(),
                            )
                            .build()
                    )
                )
            )
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
                                fieldWithPath("data.schedules[].startTime").type(JsonFieldType.STRING).description("콘서트 일시"),
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