package kr.hhplus.be.server.support.error

import org.springframework.boot.logging.LogLevel
import org.springframework.http.HttpStatus

enum class ErrorType(val status: HttpStatus, val message: String, val logLevel: LogLevel) {

    // Common
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "접근이 거부되었습니다.", LogLevel.INFO),
    DEFAULT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error has occurred.", LogLevel.ERROR),

    // Queue
    NOT_AN_ACTIVE_QUEUE(status = HttpStatus.BAD_REQUEST, message = "활성화된 대기열이 아닙니다.", logLevel = LogLevel.INFO),

    // User
    USER_NOT_FOUND(status = HttpStatus.NOT_FOUND, message = "존재하지 않는 유저입니다.", logLevel = LogLevel.WARN),

    // UserWallet
    USER_WALLET_NOT_FOUND(status = HttpStatus.NOT_FOUND, message = "존재하지 않는 지갑입니다.", logLevel = LogLevel.WARN),
    INVALID_CHARGE_AMOUNT(status = HttpStatus.BAD_REQUEST, message = "잘못된 충전 금액입니다.", logLevel = LogLevel.WARN),
    INVALID_USAGE_AMOUNT(status = HttpStatus.BAD_REQUEST, message = "잘못된 사용 금액입니다.", logLevel = LogLevel.WARN),
    LACK_OF_BALANCE(status = HttpStatus.BAD_REQUEST, message = "잔액이 부족합니다.", logLevel = LogLevel.INFO),

    // Concert
    CONCERT_NOT_FOUND(status = HttpStatus.NOT_FOUND, message = "존재하지 않는 콘서트입니다.", logLevel = LogLevel.WARN),
    CONCERT_SCHEDULE_NOT_FOUND(status = HttpStatus.NOT_FOUND, message = "존재하지 않는 콘서트 날짜입니다.", logLevel = LogLevel.WARN),
    CONCERT_SEAT_NOT_FOUND(status = HttpStatus.NOT_FOUND, message = "존재하지 않는 콘서트 좌석입니다.", logLevel = LogLevel.WARN),

    // Reservation
    RESERVATION_NOT_FOUND(status = HttpStatus.NOT_FOUND, message = "존재하지 않는 예약입니다.", logLevel = LogLevel.WARN),
    ALREADY_RESERVED_CONCERT_SEAT(status = HttpStatus.BAD_REQUEST, "이미 예약된 좌석입니다.", logLevel = LogLevel.INFO),
    NOT_PAYABLE_RESERVATION(status = HttpStatus.NOT_FOUND, message = "결제 가능한 예약이 아닙니다.", logLevel = LogLevel.WARN),
    CANNOT_ROLLBACK_PAY_RESERVATION(status = HttpStatus.NOT_FOUND, message = "취소 가능한 예약이 아닙니다.", logLevel = LogLevel.WARN),
}
