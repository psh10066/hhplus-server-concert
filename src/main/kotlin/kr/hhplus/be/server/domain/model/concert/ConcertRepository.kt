package kr.hhplus.be.server.domain.model.concert

import kr.hhplus.be.server.domain.model.common.dto.PageDto

interface ConcertRepository {

    fun findPage(page: Int, size: Int): PageDto<Concert>

    fun getById(id: Long): Concert

    fun findAllById(ids: List<Long>): List<Concert>
}