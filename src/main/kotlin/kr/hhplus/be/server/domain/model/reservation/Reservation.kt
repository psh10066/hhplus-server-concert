package kr.hhplus.be.server.domain.model.reservation

import jakarta.persistence.*
import kr.hhplus.be.server.infrastructure.dao.BaseEntity
import java.time.LocalDateTime

@Entity
@Table(name = "reservation")
class Reservation(
    @Column(nullable = false)
    val concertScheduleId: Long,

    @Column(nullable = false)
    val concertSeatId: Long,

    @Column(nullable = false)
    val userId: Long,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val status: ReservationStatus,

    val expiredAt: LocalDateTime? = null,
) : BaseEntity()
