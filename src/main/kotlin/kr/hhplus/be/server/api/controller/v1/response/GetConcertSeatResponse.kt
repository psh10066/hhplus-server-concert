package kr.hhplus.be.server.api.controller.v1.response

import kr.hhplus.be.server.domain.model.concert.ConcertSeat

data class GetConcertSeatResponse(
    val seats: List<SeatDto>
) {
    data class SeatDto(
        val concertSeatId: Long,
        val seatNumber: Int
    ) {
        companion object {
            fun of(seat: ConcertSeat): SeatDto {
                return SeatDto(
                    concertSeatId = seat.id,
                    seatNumber = seat.seatNumber
                )
            }
        }
    }
}