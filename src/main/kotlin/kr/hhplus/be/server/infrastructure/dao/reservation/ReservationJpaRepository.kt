package kr.hhplus.be.server.infrastructure.dao.reservation

import kr.hhplus.be.server.domain.model.reservation.Reservation
import org.springframework.data.jpa.repository.JpaRepository

interface ReservationJpaRepository : JpaRepository<Reservation, Long>