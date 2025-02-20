package kr.hhplus.be.server.domain.model.reservation

interface ReservationOutboxRepository {

    fun save(reservationOutbox: ReservationOutbox): ReservationOutbox

    fun findByOperationTypeAndOperationId(operationType: String, operationId: String): ReservationOutbox?
}