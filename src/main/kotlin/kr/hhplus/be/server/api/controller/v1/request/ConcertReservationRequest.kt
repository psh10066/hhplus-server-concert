package kr.hhplus.be.server.api.controller.v1.request

data class ConcertReservationRequest(
    val concertScheduleId: Long,
    val concertSeatId: Long
)