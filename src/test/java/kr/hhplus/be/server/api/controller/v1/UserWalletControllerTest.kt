package kr.hhplus.be.server.api.controller.v1

import com.epages.restdocs.apispec.ResourceDocumentation.resource
import com.epages.restdocs.apispec.ResourceSnippetParameters
import com.epages.restdocs.apispec.Schema
import kr.hhplus.be.server.api.RestDocsTest
import kr.hhplus.be.server.api.controller.v1.request.ChargeBalanceRequest
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class UserWalletControllerTest : RestDocsTest() {

    @Test
    fun getBalance() {
        mockMvc.perform(
            get("/api/v1/user-wallets/balance")
                .header("userId", 1L)
        )
            .andExpect(status().isOk)
            .andDo(
                document(
                    "잔액 조회",
                    resource(
                        ResourceSnippetParameters.builder()
                            .tag("유저 지갑")
                            .summary("잔액 조회 API")
                            .description("유저 잔액 조회 API")
                            .requestHeaders(
                                headerWithName("userId").description("유저 ID")
                            )
                            .responseSchema(Schema("UserWalletBalanceResponse"))
                            .responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("요청 성공 여부"),
                                fieldWithPath("data.userId").type(JsonFieldType.NUMBER).description("유저 ID"),
                                fieldWithPath("data.balance").type(JsonFieldType.NUMBER).description("잔액"),
                                fieldWithPath("error").type(JsonFieldType.NULL).ignored(),
                            )
                            .build()
                    )
                )
            )
    }

    @Test
    fun chargeBalance() {
        mockMvc.perform(
            patch("/api/v1/user-wallets/balance")
                .header("userId", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ChargeBalanceRequest(amount = 1000L)))
        )
            .andExpect(status().isOk)
            .andDo(
                document(
                    "잔액 충전",
                    resource(
                        ResourceSnippetParameters.builder()
                            .tag("유저 지갑")
                            .summary("잔액 충전 API")
                            .description("유저 잔액 충전 API")
                            .requestHeaders(
                                headerWithName("userId").description("유저 ID")
                            )
                            .requestSchema(Schema("ChargeBalanceRequest"))
                            .requestFields(
                                fieldWithPath("amount").type(JsonFieldType.NUMBER).description("증액량"),
                            )
                            .responseSchema(Schema("SuccessResponse"))
                            .responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("요청 성공 여부"),
                                fieldWithPath("data").type(JsonFieldType.NULL).ignored(),
                                fieldWithPath("error").type(JsonFieldType.NULL).ignored(),
                            )
                            .build()
                    )
                )
            )
    }
}