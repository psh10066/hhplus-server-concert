package kr.hhplus.be.server.infrastructure.dao.reservation

import org.springframework.data.jpa.repository.JpaRepository

interface ReservationOutboxJpaRepository : JpaRepository<ReservationOutboxEntity, Long> {

    fun findByOperationTypeAndOperationId(operationType: String, operationId: String): ReservationOutboxEntity?
}