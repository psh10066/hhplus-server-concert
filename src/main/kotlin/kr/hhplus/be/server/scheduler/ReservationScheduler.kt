package kr.hhplus.be.server.scheduler

import kr.hhplus.be.server.domain.model.concert.Concert
import kr.hhplus.be.server.domain.service.ReservationService
import org.springframework.cache.annotation.CachePut
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

    @CachePut(value = ["popularConcerts"])
    @Scheduled(fixedRate = 30000, initialDelay = 0)
    fun cachePreWarmingPopularConcerts(): List<Concert> {
        return reservationService.getPopularConcerts()
    }
}