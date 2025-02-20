package kr.hhplus.be.server.infrastructure.dao.reservation

import kr.hhplus.be.server.domain.model.reservation.ReservationOutboxStatus
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface ReservationOutboxJpaRepository : JpaRepository<ReservationOutboxEntity, Long> {

    fun findByOperationTypeAndOperationId(operationType: String, operationId: String): ReservationOutboxEntity?

    fun findAllByStatusAndCreatedAtBefore(status: ReservationOutboxStatus, createdAtAfter: LocalDateTime): List<ReservationOutboxEntity>
}