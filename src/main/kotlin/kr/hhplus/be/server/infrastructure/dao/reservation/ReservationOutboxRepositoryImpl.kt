package kr.hhplus.be.server.infrastructure.dao.reservation

import kr.hhplus.be.server.domain.model.reservation.ReservationOutbox
import kr.hhplus.be.server.domain.model.reservation.ReservationOutboxRepository
import org.springframework.stereotype.Component

@Component
class ReservationOutboxRepositoryImpl(
    private val reservationOutboxJpaRepository: ReservationOutboxJpaRepository
) : ReservationOutboxRepository {

    override fun save(reservationOutbox: ReservationOutbox): ReservationOutbox {
        val from = ReservationOutboxEntity.from(reservationOutbox)
        return reservationOutboxJpaRepository.save(from).toModel()
    }

    override fun findByOperationTypeAndOperationId(operationType: String, operationId: String): ReservationOutbox? {
        return reservationOutboxJpaRepository.findByOperationTypeAndOperationId(operationType, operationId)?.toModel()
    }
}