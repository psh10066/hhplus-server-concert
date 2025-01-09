package kr.hhplus.be.server.api.controller.v1

import kr.hhplus.be.server.api.controller.v1.common.dto.PageResponseDto
import kr.hhplus.be.server.api.controller.v1.response.GetConcertResponse
import kr.hhplus.be.server.api.controller.v1.response.GetConcertScheduleResponse
import kr.hhplus.be.server.api.controller.v1.response.GetConcertSeatResponse
import kr.hhplus.be.server.domain.model.queue.dto.QueueInfo
import kr.hhplus.be.server.domain.service.ConcertService
import kr.hhplus.be.server.support.response.ApiResponse
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/api/v1/concerts")
class ConcertController(
    private val concertService: ConcertService
) {

    @GetMapping
    fun getConcerts(
        @RequestParam page: Int,
        @RequestParam size: Int,
        queueInfo: QueueInfo
    ): ApiResponse<GetConcertResponse> {
        val concerts = concertService.findPage(page, size)
        val response = GetConcertResponse(PageResponseDto.map(concerts, {
            GetConcertResponse.ConcertDto.of(it)
        }))
        return ApiResponse.success(response)
    }

    @GetMapping("/{concertId}/schedules")
    fun getSchedules(
        @PathVariable concertId: Long,
        queueInfo: QueueInfo
    ): ApiResponse<GetConcertScheduleResponse> {
        return ApiResponse.success(
            GetConcertScheduleResponse(
                listOf(
                    GetConcertScheduleResponse.ScheduleDto(concertScheduleId = 1L, date = LocalDate.of(2025, 1, 1)),
                    GetConcertScheduleResponse.ScheduleDto(concertScheduleId = 2L, date = LocalDate.of(2025, 1, 2)),
                )
            )
        )
    }

    @GetMapping("/schedules/{concertScheduleId}/seats")
    fun getSeats(
        @PathVariable concertScheduleId: Long,
        queueInfo: QueueInfo
    ): ApiResponse<GetConcertSeatResponse> {
        return ApiResponse.success(
            GetConcertSeatResponse(
                listOf(
                    GetConcertSeatResponse.SeatDto(concertSeatId = 1L, seatNumber = 11),
                    GetConcertSeatResponse.SeatDto(concertSeatId = 2L, seatNumber = 12),
                )
            )
        )
    }
}