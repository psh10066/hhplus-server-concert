package kr.hhplus.be.server.infrastructure.dao.concert

import com.querydsl.jpa.impl.JPAQueryFactory
import kr.hhplus.be.server.domain.model.concert.ConcertSeat
import kr.hhplus.be.server.domain.model.concert.QConcert.concert
import kr.hhplus.be.server.domain.model.concert.QConcertSchedule.concertSchedule
import kr.hhplus.be.server.domain.model.concert.QConcertSeat.concertSeat
import kr.hhplus.be.server.domain.model.queue.QQueue.queue
import kr.hhplus.be.server.domain.model.reservation.QReservation.reservation
import java.time.Clock
import java.time.LocalDateTime

class ConcertSeatCustomRepositoryImpl(
    private val clock: Clock,
    private val queryFactory: JPAQueryFactory
) : ConcertSeatCustomRepository {

    override fun findAvailableSeats(concertScheduleId: Long): List<ConcertSeat> {
        return queryFactory
            .select(concertSeat)
            .from(concertSchedule)
            .join(concert).on(concert.id.eq(concertSchedule.concertId))
            .join(concertSeat).on(concertSeat.concertId.eq(concert.id))
            .leftJoin(reservation).on(
                reservation.concertScheduleId.eq(concertSchedule.id),
                reservation.concertSeatId.eq(concertSeat.id),
                reservation.expiredAt.isNull.or(reservation.expiredAt.after(LocalDateTime.now(clock)))
            )
            .where(reservation.isNull)
            .orderBy(concertSeat.seatNumber.asc())
            .fetch()
    }
}