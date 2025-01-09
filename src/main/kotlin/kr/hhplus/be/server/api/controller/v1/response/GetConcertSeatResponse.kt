package kr.hhplus.be.server.api.controller.v1.response

import kr.hhplus.be.server.domain.model.concert.dto.ConcertSeatInfo

data class GetConcertSeatResponse(
    val seats: List<SeatDto>
) {
    data class SeatDto(
        val concertSeatId: Long,
        val seatNumber: Int
    ) {
        companion object {
            fun of(seat: ConcertSeatInfo): SeatDto {
                return SeatDto(
                    concertSeatId = seat.id,
                    seatNumber = seat.seatNumber
                )
            }
        }
    }
}