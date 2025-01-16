package kr.hhplus.be.server.api.controller.v1

import kr.hhplus.be.server.api.controller.v1.response.IssueQueueResponse
import kr.hhplus.be.server.domain.model.user.User
import kr.hhplus.be.server.domain.service.QueueService
import kr.hhplus.be.server.support.response.ApiResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/queues")
class QueueController(
    private val queueService: QueueService
) {

    @PostMapping("/token")
    fun issueToken(
        user: User
    ): ApiResponse<IssueQueueResponse> {
        val token = queueService.issueToken(user.uuid)
        return ApiResponse.success(IssueQueueResponse(token = token))
    }
}