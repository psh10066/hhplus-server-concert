package kr.hhplus.be.server.api.controller.v1

import kr.hhplus.be.server.api.controller.v1.common.dto.PageResponseDto
import kr.hhplus.be.server.api.controller.v1.response.GetConcertResponse
import kr.hhplus.be.server.api.controller.v1.response.GetConcertScheduleResponse
import kr.hhplus.be.server.api.controller.v1.response.GetConcertSeatResponse
import kr.hhplus.be.server.domain.model.queue.Queue
import kr.hhplus.be.server.domain.service.ConcertService
import kr.hhplus.be.server.support.response.ApiResponse
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/concerts")
class ConcertController(
    private val concertService: ConcertService
) {

    @GetMapping
    fun getConcerts(
        @RequestParam page: Int,
        @RequestParam size: Int,
        queue: Queue
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
        queue: Queue
    ): ApiResponse<GetConcertScheduleResponse> {
        val concertSchedules = concertService.findConcertSchedules(concertId)
        return ApiResponse.success(
            GetConcertScheduleResponse(
                concertSchedules.map { GetConcertScheduleResponse.ScheduleDto.of(it) }
            )
        )
    }

    @GetMapping("/schedules/{concertScheduleId}/seats")
    fun getSeats(
        @PathVariable concertScheduleId: Long,
        queue: Queue
    ): ApiResponse<GetConcertSeatResponse> {
        val concertSeats = concertService.findAvailableSeats(concertScheduleId)
        return ApiResponse.success(
            GetConcertSeatResponse(
                concertSeats.map { GetConcertSeatResponse.SeatDto.of(it) }
            )
        )
    }
}