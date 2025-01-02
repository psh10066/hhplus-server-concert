package kr.hhplus.be.server.api.controller.v1

import kr.hhplus.be.server.api.controller.v1.response.IssueQueueResponse
import kr.hhplus.be.server.support.response.ApiResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/queues")
class QueueController {

    @PostMapping("/token")
    fun issueToken(
        @RequestHeader userId: Long
    ): ApiResponse<IssueQueueResponse> {
        return ApiResponse.success(IssueQueueResponse(token = "token:123"))
    }
}