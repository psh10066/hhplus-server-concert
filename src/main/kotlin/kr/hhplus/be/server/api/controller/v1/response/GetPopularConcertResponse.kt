package kr.hhplus.be.server.api.controller.v1.response

import kr.hhplus.be.server.domain.model.concert.Concert

data class GetPopularConcertResponse(
    val concerts: List<ConcertDto>,
) {
    data class ConcertDto(
        val id: Long,
        val name: String,
        val price: Long
    ) {
        companion object {
            fun of(concert: Concert): ConcertDto {
                return ConcertDto(
                    id = concert.id,
                    name = concert.name,
                    price = concert.price
                )
            }
        }
    }
}