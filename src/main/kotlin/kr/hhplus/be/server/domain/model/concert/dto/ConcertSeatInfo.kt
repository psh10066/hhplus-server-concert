package kr.hhplus.be.server.domain.model.concert.dto

import kr.hhplus.be.server.domain.model.concert.ConcertSeat

data class ConcertSeatInfo(
    val id: Long,
    val concertId: Long,
    val seatNumber: Int
) {
    companion object {
        fun of(concertSeat: ConcertSeat): ConcertSeatInfo {
            return ConcertSeatInfo(
                id = concertSeat.id,
                concertId = concertSeat.concertId,
                seatNumber = concertSeat.seatNumber
            )
        }
    }
}
