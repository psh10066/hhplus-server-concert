package kr.hhplus.be.server.infrastructure.dao.common

import kr.hhplus.be.server.domain.model.common.dto.PageDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

fun <T> Page<T>.toDto(): PageDto<T> {
    return PageDto(
        totalElements = this.totalElements,
        totalPages = this.totalPages,
        content = this.content
    )
}

fun getOneBasedPageable(page: Int, size: Int, sort: Sort = Sort.unsorted()): Pageable {
    return PageRequest.of(page - 1, size, sort)
}