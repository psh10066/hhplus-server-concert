package kr.hhplus.be.server.api.controller.v1

import com.epages.restdocs.apispec.ResourceDocumentation.resource
import com.epages.restdocs.apispec.ResourceSnippetParameters
import com.epages.restdocs.apispec.Schema
import kr.hhplus.be.server.api.ControllerIntegrationTest
import kr.hhplus.be.server.api.controller.v1.request.ConcertPaymentRequest
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class PaymentControllerTest : ControllerIntegrationTest() {

    @Test
    fun concertPayment() {
        mockMvc.perform(
            post("/api/v1/payment/concert")
                .header("token", "token:123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ConcertPaymentRequest(concertReservationId = 1L)))
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
                                fieldWithPath("concertReservationId").type(JsonFieldType.NUMBER).description("콘서트 예약 ID"),
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