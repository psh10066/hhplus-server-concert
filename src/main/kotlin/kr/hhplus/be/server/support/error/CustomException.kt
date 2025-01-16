package kr.hhplus.be.server.support.error

class CustomException(
    val errorType: ErrorType,
) : RuntimeException(errorType.message)
