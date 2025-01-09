package kr.hhplus.be.server.scheduler

import kr.hhplus.be.server.domain.service.QueueService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class QueueScheduler(
    private val queueService: QueueService
) {

    @Scheduled(fixedRate = 10000)
    fun activateQueue() {
        queueService.activate()
    }
}