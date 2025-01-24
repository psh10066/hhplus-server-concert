package kr.hhplus.be.server.infrastructure.dao.concert

import jakarta.persistence.*
import kr.hhplus.be.server.domain.model.concert.ConcertSeat
import kr.hhplus.be.server.domain.model.concert.ConcertSeatStatus
import kr.hhplus.be.server.infrastructure.dao.BaseEntity

@Entity
@Table(name = "concert_seat")
class ConcertSeatEntity(
    id: Long = 0,

    @Column(nullable = false)
    val concertScheduleId: Long,

    @Column(nullable = false)
    val seatNumber: Int,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val status: ConcertSeatStatus = ConcertSeatStatus.AVAILABLE,

    @Version
    @Column(nullable = false)
    val version: Long = 0,
) : BaseEntity(id) {

    fun toModel(): ConcertSeat {
        return ConcertSeat(
            id = id,
            concertScheduleId = concertScheduleId,
            seatNumber = seatNumber,
            status = status,
            version = version,
        )
    }

    companion object {
        fun from(concertSeat: ConcertSeat): ConcertSeatEntity {
            return ConcertSeatEntity(
                id = concertSeat.id,
                concertScheduleId = concertSeat.concertScheduleId,
                seatNumber = concertSeat.seatNumber,
                status = concertSeat.status,
                version = concertSeat.version,
            )
        }
    }
}