package kr.hhplus.be.server.api.controller.v1.response

import com.fasterxml.jackson.annotation.JsonFormat
import kr.hhplus.be.server.domain.model.concert.ConcertSchedule
import java.time.LocalDateTime

data class GetConcertScheduleResponse(
    val schedules: List<ScheduleDto>
) {
    data class ScheduleDto(
        val concertScheduleId: Long,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, locale = "Asia/Seoul")
        val startTime: LocalDateTime
    ) {
        companion object {
            fun of(concertSchedule: ConcertSchedule): ScheduleDto {
                return ScheduleDto(
                    concertScheduleId = concertSchedule.id,
                    startTime = concertSchedule.startTime
                )
            }
        }
    }
}