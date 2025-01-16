package kr.hhplus.be.server.infrastructure.dao.concert

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import kr.hhplus.be.server.domain.model.concert.ConcertSeat
import kr.hhplus.be.server.infrastructure.dao.BaseEntity

@Entity
@Table(name = "concert_seat")
class ConcertSeatEntity(
    id: Long = 0,

    @Column(nullable = false)
    val concertScheduleId: Long,

    @Column(nullable = false)
    val seatNumber: Int,
) : BaseEntity(id) {

    fun toModel(): ConcertSeat {
        return ConcertSeat(
            id = id,
            concertScheduleId = concertScheduleId,
            seatNumber = seatNumber,
        )
    }

    companion object {
        fun from(concertSeat: ConcertSeat): ConcertSeatEntity {
            return ConcertSeatEntity(
                id = concertSeat.id,
                concertScheduleId = concertSeat.concertScheduleId,
                seatNumber = concertSeat.seatNumber,
            )
        }
    }
}