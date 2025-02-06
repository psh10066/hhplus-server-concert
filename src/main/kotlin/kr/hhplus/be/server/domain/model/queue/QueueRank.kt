package kr.hhplus.be.server.domain.model.queue

data class QueueRank private constructor(
    val status: QueueStatus,
    val rank: Long?,
) {
    companion object {
        fun waiting(rank: Long): QueueRank {
            return QueueRank(
                status = QueueStatus.WAITING,
                rank = rank
            )
        }

        fun active(): QueueRank {
            return QueueRank(
                status = QueueStatus.ACTIVE,
                rank = null
            )
        }
    }
}