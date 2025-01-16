package kr.hhplus.be.server.infrastructure.dao.concert

import com.querydsl.jpa.impl.JPAQueryFactory
import kr.hhplus.be.server.infrastructure.dao.concert.QConcertScheduleEntity.concertScheduleEntity
import kr.hhplus.be.server.infrastructure.dao.concert.QConcertSeatEntity.concertSeatEntity
import kr.hhplus.be.server.infrastructure.dao.reservation.QReservationEntity.reservationEntity
import java.time.Clock
import java.time.LocalDateTime

class ConcertSeatCustomRepositoryImpl(
    private val clock: Clock,
    private val queryFactory: JPAQueryFactory
) : ConcertSeatCustomRepository {

    override fun findAvailableSeats(concertScheduleId: Long): List<ConcertSeatEntity> {
        return queryFactory
            .select(concertSeatEntity)
            .from(concertScheduleEntity)
            .join(concertSeatEntity).on(concertSeatEntity.concertScheduleId.eq(concertScheduleEntity.id))
            .leftJoin(reservationEntity).on(
                reservationEntity.concertSeatId.eq(concertSeatEntity.id),
                reservationEntity.expiredAt.isNull.or(reservationEntity.expiredAt.after(LocalDateTime.now(clock)))
            )
            .where(
                reservationEntity.isNull,
                concertScheduleEntity.id.eq(concertScheduleId),
            )
            .orderBy(concertSeatEntity.seatNumber.asc())
            .fetch()
    }
}