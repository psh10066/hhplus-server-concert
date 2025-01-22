package kr.hhplus.be.server.api.controller.v1

import com.epages.restdocs.apispec.ResourceDocumentation.resource
import com.epages.restdocs.apispec.ResourceSnippetParameters
import com.epages.restdocs.apispec.Schema
import kr.hhplus.be.server.api.ControllerIntegrationTest
import kr.hhplus.be.server.api.controller.v1.request.ConcertPaymentRequest
import kr.hhplus.be.server.api.controller.v1.request.ConcertReservationRequest
import kr.hhplus.be.server.domain.model.reservation.ReservationStatus
import kr.hhplus.be.server.infrastructure.dao.concert.*
import kr.hhplus.be.server.infrastructure.dao.reservation.ReservationEntity
import kr.hhplus.be.server.infrastructure.dao.reservation.ReservationJpaRepository
import kr.hhplus.be.server.infrastructure.dao.user.UserWalletEntity
import kr.hhplus.be.server.infrastructure.dao.user.UserWalletJpaRepository
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
    @Autowired private val userWalletJpaRepository: UserWalletJpaRepository,
    @Autowired private val concertJpaRepository: ConcertJpaRepository,
    @Autowired private val concertScheduleJpaRepository: ConcertScheduleJpaRepository,
    @Autowired private val concertSeatJpaRepository: ConcertSeatJpaRepository,
    @Autowired private val reservationJpaRepository: ReservationJpaRepository
) : ControllerIntegrationTest() {

    @BeforeEach
    fun setUp() {
        userWalletJpaRepository.save(UserWalletEntity(userId = 1L, balance = 1000000L))
        concertJpaRepository.save(ConcertEntity(name = "아이유 콘서트", price = 150000L))
        concertScheduleJpaRepository.save(ConcertScheduleEntity(concertId = 1L, startTime = LocalDateTime.of(2025, 1, 8, 11, 0)))
        concertSeatJpaRepository.save(ConcertSeatEntity(concertScheduleId = 1L, seatNumber = 1))
        concertSeatJpaRepository.save(ConcertSeatEntity(concertScheduleId = 1L, seatNumber = 2))
        concertSeatJpaRepository.save(ConcertSeatEntity(concertScheduleId = 1L, seatNumber = 3))
        reservationJpaRepository.save(ReservationEntity(concertSeatId = 3L, userId = 1L, status = ReservationStatus.RESERVED, expiredAt = LocalDateTime.now().plusMinutes(3)))
    }

    @Test
    fun concertReservation() {
        mockMvc.perform(
            post("/api/v1/reservations/concert")
                .header("token", "token:123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ConcertReservationRequest(concertSeatId = 2L)))
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

    @Test
    fun concertPayment() {
        mockMvc.perform(
            post("/api/v1/reservations/concert/payment")
                .header("token", "token:123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ConcertPaymentRequest(reservationId = 1L)))
        )
            .andExpect(status().isOk)
            .andDo(
                document(
                    "콘서트 결제",
                    resource(
                        ResourceSnippetParameters.builder()
                            .tag("결제")
                            .summary("콘서트 결제 API")
                            .description("예약된 콘서트 좌석 결제 API")
                            .requestHeaders(
                                headerWithName("token").description("대기열 토큰")
                            )
                            .requestSchema(Schema("ConcertPaymentRequest"))
                            .requestFields(
                                fieldWithPath("reservationId").type(JsonFieldType.NUMBER).description("예약 ID"),
                            )
                            .responseSchema(Schema("ConcertPaymentResponse"))
                            .responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("요청 성공 여부"),
                                fieldWithPath("data.paymentHistoryId").type(JsonFieldType.NUMBER).description("결제 이력 ID"),
                                fieldWithPath("error").type(JsonFieldType.NULL).ignored(),
                            )
                            .build()
                    )
                )
            )
    }
}