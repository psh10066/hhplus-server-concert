package kr.hhplus.be.server.api.controller.v1.response

import kr.hhplus.be.server.api.controller.v1.common.dto.PageResponseDto
import kr.hhplus.be.server.domain.model.concert.Concert

data class GetConcertResponse(
    val concerts: PageResponseDto<ConcertDto>,
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