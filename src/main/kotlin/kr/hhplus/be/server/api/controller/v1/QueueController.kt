package kr.hhplus.be.server.api.controller.v1

import kr.hhplus.be.server.api.controller.v1.response.GetQueueRankResponse
import kr.hhplus.be.server.api.controller.v1.response.IssueQueueResponse
import kr.hhplus.be.server.domain.model.user.User
import kr.hhplus.be.server.domain.service.QueueService
import kr.hhplus.be.server.support.response.ApiResponse
import org.springframework.web.bind.annotation.*

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

    @GetMapping("/token/rank")
    fun getQueueRank(
        @RequestHeader token: String,
    ): ApiResponse<GetQueueRankResponse> {
        val queueRank = queueService.getQueueRank(token)
        return ApiResponse.success(GetQueueRankResponse(
            status = queueRank.status,
            rank = queueRank.rank
        ))
    }
}