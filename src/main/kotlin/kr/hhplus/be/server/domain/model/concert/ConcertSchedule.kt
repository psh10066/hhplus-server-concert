package kr.hhplus.be.server.domain.model.concert

import java.time.LocalDateTime

class ConcertSchedule(
    val id: Long = 0,
    val concertId: Long,
    val startTime: LocalDateTime,
)