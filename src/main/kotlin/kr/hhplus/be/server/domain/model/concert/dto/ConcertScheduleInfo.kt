package kr.hhplus.be.server.domain.model.concert.dto

import kr.hhplus.be.server.domain.model.concert.ConcertSchedule
import java.time.LocalDateTime

data class ConcertScheduleInfo(
    val id: Long,
    val concertId: Long,
    val startTime: LocalDateTime
) {
    companion object {
        fun of(concertSchedule: ConcertSchedule): ConcertScheduleInfo {
            return ConcertScheduleInfo(
                id = concertSchedule.id,
                concertId = concertSchedule.concertId,
                startTime = concertSchedule.startTime
            )
        }
    }
}
