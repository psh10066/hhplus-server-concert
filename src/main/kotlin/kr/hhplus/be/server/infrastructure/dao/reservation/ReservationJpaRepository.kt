package kr.hhplus.be.server.infrastructure.dao.reservation

import org.springframework.data.jpa.repository.JpaRepository

interface ReservationJpaRepository : JpaRepository<ReservationEntity, Long> {

    fun findByConcertSeatId(concertSeatId: Long): List<ReservationEntity>
}