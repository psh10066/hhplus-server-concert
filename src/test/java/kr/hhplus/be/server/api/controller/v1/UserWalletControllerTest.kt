package kr.hhplus.be.server.api.controller.v1

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document
import kr.hhplus.be.server.api.RestDocsTest
import kr.hhplus.be.server.api.controller.v1.request.ChargeBalanceRequest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders
import org.springframework.restdocs.operation.preprocess.Preprocessors.*
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class UserWalletControllerTest : RestDocsTest() {

    private lateinit var controller: UserWalletController

    @BeforeEach
    fun setUp() {
        controller = UserWalletController()
        mockMvc = mockController(controller)
    }

    @Test
    fun getBalance() {
        mockMvc.perform(
            get("/api/v1/user-wallets/balance")
                .header("userId", 1L)
        )
            .andExpect(status().isOk)
            .andDo(
                document(
                    "api/v1/user-wallets/balance",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestHeaders(
                        headerWithName("userId").description("유저 ID")
                    ),
                    responseFields(
                        fieldWithPath("result").type(JsonFieldType.STRING).description("요청 성공 여부"),
                        fieldWithPath("data.userId").type(JsonFieldType.NUMBER).description("유저 ID"),
                        fieldWithPath("data.balance").type(JsonFieldType.NUMBER).description("잔액"),
                        fieldWithPath("error").type(JsonFieldType.NULL).ignored(),
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
                    "api/v1/user-wallets/balance/charge",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestHeaders(
                        headerWithName("userId").description("유저 ID")
                    ),
                    requestFields(
                        fieldWithPath("amount").type(JsonFieldType.NUMBER).description("증액량"),
                    ),
                    responseFields(
                        fieldWithPath("result").type(JsonFieldType.STRING).description("요청 성공 여부"),
                        fieldWithPath("data").type(JsonFieldType.NULL).ignored(),
                        fieldWithPath("error").type(JsonFieldType.NULL).ignored(),
                    )
                )
            )
    }
}