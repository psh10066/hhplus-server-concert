package kr.hhplus.be.server.infrastructure.dao.concert

import kr.hhplus.be.server.domain.model.common.dto.PageDto
import kr.hhplus.be.server.domain.model.concert.Concert
import kr.hhplus.be.server.domain.model.concert.ConcertRepository
import kr.hhplus.be.server.infrastructure.dao.common.getOneBasedPageable
import kr.hhplus.be.server.infrastructure.dao.common.toDto
import org.springframework.stereotype.Component

@Component
class ConcertRepositoryImpl(
    private val concertJpaRepository: ConcertJpaRepository
) : ConcertRepository {

    override fun findPage(page: Int, size: Int): PageDto<Concert> {
        val pageRequest = getOneBasedPageable(page, size)
        return concertJpaRepository.findAll(pageRequest).toDto()
    }
}