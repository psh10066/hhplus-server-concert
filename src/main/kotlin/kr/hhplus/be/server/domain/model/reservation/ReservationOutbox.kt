package kr.hhplus.be.server.domain.model.reservation

class ReservationOutbox(
    val id: Long = 0,
    val operationType: String,
    val operationId: String,
    val topic: String,
    val key: String?,
    val message: String,
    var status: ReservationOutboxStatus,
) {
    companion object {
        fun create(
            operationType: String,
            operationId: String,
            topic: String,
            topicKey: String? = null,
            message: String
        ): ReservationOutbox {
            return ReservationOutbox(
                operationType = operationType,
                operationId = operationId,
                topic = topic,
                key = topicKey,
                message = message,
                status = ReservationOutboxStatus.CREATED,
            )
        }
    }
}
