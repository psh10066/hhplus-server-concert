package kr.hhplus.be.server.api.controller.v1.response

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDate

data class GetConcertScheduleResponse(
    val schedules: List<ScheduleDto>
) {
    data class ScheduleDto(
        val concertScheduleId: Long,
        @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING, locale = "Asia/Seoul")
        val date: LocalDate
    )
}