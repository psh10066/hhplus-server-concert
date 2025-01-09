package kr.hhplus.be.server.domain.model.common.dto

data class PageDto<T>(
    val totalElements: Long,
    val totalPages: Int,
    val content: List<T>
) {
    fun <R> map(transform: (T) -> R): PageDto<R> {
        return PageDto(
            totalElements = this.totalElements,
            totalPages = this.totalPages,
            content = this.content.map(transform)
        )
    }
}