package kr.hhplus.be.server.support.response

data class ApiResponse<T> private constructor(
    val result: ResultType,
    val data: T? = null,
    val error: Any? = null,
) {
    companion object {
        fun success(): ApiResponse<Any> {
            return ApiResponse(ResultType.SUCCESS, null, null)
        }

        fun <S> success(data: S): ApiResponse<S> {
            return ApiResponse(ResultType.SUCCESS, data, null)
        }
    }
}
