package kr.hhplus.be.server.domain.model.reservation

import java.time.LocalDateTime

interface ReservationOutboxRepository {

    fun save(reservationOutbox: ReservationOutbox): ReservationOutbox

    fun findByOperationTypeAndOperationId(operationType: String, operationId: String): ReservationOutbox?

    fun findAllByStatusAndCreatedAtBefore(status: ReservationOutboxStatus, localDateTime: LocalDateTime): List<ReservationOutbox>
}