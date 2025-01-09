package kr.hhplus.be.server.domain.model.concert.dto

import kr.hhplus.be.server.domain.model.concert.Concert

data class ConcertInfo(
    val id: Long,
    val name: String,
    val price: Long
) {
    companion object {
        fun of(concert: Concert): ConcertInfo {
            return ConcertInfo(
                id = concert.id,
                name = concert.name,
                price = concert.price
            )
        }
    }
}
