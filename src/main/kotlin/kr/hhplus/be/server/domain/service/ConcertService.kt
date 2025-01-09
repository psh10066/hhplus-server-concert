package kr.hhplus.be.server.domain.service

import kr.hhplus.be.server.domain.model.common.dto.PageDto
import kr.hhplus.be.server.domain.model.concert.ConcertRepository
import kr.hhplus.be.server.domain.model.concert.dto.ConcertInfo
import org.springframework.stereotype.Service

@Service
class ConcertService(
    private val concertRepository: ConcertRepository
) {

    fun findPage(page: Int, size: Int): PageDto<ConcertInfo> {
        return concertRepository.findPage(page, size).map { ConcertInfo.of(it) }
    }
}