package kr.hhplus.be.server.scheduler

import kr.hhplus.be.server.domain.service.ReservationService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class ReservationScheduler(
    private val reservationService: ReservationService
) {

    @Scheduled(fixedRate = 10000)
    fun expireReservation() {
        reservationService.expireReservations()
    }
}