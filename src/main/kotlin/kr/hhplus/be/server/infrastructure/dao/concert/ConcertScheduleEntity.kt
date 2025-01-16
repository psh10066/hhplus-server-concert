package kr.hhplus.be.server.infrastructure.dao.concert

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import kr.hhplus.be.server.domain.model.concert.ConcertSchedule
import kr.hhplus.be.server.infrastructure.dao.BaseEntity
import java.time.LocalDateTime

@Entity
@Table(name = "concert_schedule")
class ConcertScheduleEntity(
    id: Long = 0,

    @Column(nullable = false)
    val concertId: Long,

    @Column(nullable = false)
    val startTime: LocalDateTime,
) : BaseEntity(id) {

    fun toModel(): ConcertSchedule {
        return ConcertSchedule(
            id = id,
            concertId = concertId,
            startTime = startTime,
        )
    }

    companion object {
        fun from(concertSchedule: ConcertSchedule): ConcertScheduleEntity {
            return ConcertScheduleEntity(
                id = concertSchedule.id,
                concertId = concertSchedule.concertId,
                startTime = concertSchedule.startTime,
            )
        }
    }
}