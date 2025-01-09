package kr.hhplus.be.server.api.controller.v1.common.dto

import kr.hhplus.be.server.domain.model.common.dto.PageDto

data class PageResponseDto<T>(
    val totalElements: Long,
    val totalPages: Int,
    val content: List<T>
) {
    companion object {
        fun <T, R> map(page: PageDto<T>, transform: (T) -> R): PageResponseDto<R> {
            return PageResponseDto(
                totalElements = page.totalElements,
                totalPages = page.totalPages,
                content = page.content.map(transform)
            )
        }
    }
}
