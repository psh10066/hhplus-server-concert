package kr.hhplus.be.server.scheduler

import kr.hhplus.be.server.application.ReservationFacade
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class ReservationScheduler(
    private val reservationFacade: ReservationFacade
) {

    @Scheduled(fixedRate = 10000)
    fun expireReservation() {
        reservationFacade.expireReservations()
    }
}