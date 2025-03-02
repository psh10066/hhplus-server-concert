package kr.hhplus.be.server.infrastructure.dao.concert

import kr.hhplus.be.server.domain.model.common.dto.PageDto
import kr.hhplus.be.server.domain.model.concert.Concert
import kr.hhplus.be.server.domain.model.concert.ConcertRepository
import kr.hhplus.be.server.infrastructure.dao.common.getOneBasedPageable
import kr.hhplus.be.server.infrastructure.dao.common.toDto
import kr.hhplus.be.server.support.error.CustomException
import kr.hhplus.be.server.support.error.ErrorType
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class ConcertRepositoryImpl(
    private val concertJpaRepository: ConcertJpaRepository
) : ConcertRepository {

    override fun findPage(page: Int, size: Int): PageDto<Concert> {
        val pageRequest = getOneBasedPageable(page, size)
        return concertJpaRepository.findAll(pageRequest).toDto().map {
            it.toModel()
        }
    }

    override fun getById(id: Long): Concert {
        return concertJpaRepository.findByIdOrNull(id)?.toModel()
            ?: throw CustomException(ErrorType.CONCERT_NOT_FOUND)
    }

    override fun findAllById(ids: List<Long>): List<Concert> {
        return concertJpaRepository.findAllById(ids).map {
            it.toModel()
        }
    }
}