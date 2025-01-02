package kr.hhplus.be.server.api.controller.v1.response

data class GetConcertSeatResponse(
    val seats: List<SeatDto>
) {
    data class SeatDto(
        val concertSeatId: Long,
        val seatNumber: Int
    )
}