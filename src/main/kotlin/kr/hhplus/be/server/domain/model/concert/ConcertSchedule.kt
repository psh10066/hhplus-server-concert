package kr.hhplus.be.server.domain.model.concert

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import kr.hhplus.be.server.infrastructure.dao.BaseEntity
import java.time.LocalDateTime

@Entity
@Table(name = "concert_schedule")
class ConcertSchedule(
    @Column(nullable = false)
    val concertId: Long,

    @Column(nullable = false)
    val startTime: LocalDateTime,
) : BaseEntity()