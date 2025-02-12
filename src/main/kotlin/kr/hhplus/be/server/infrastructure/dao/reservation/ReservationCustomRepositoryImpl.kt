package kr.hhplus.be.server.infrastructure.dao.reservation

import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import kr.hhplus.be.server.domain.model.reservation.ConcertReservationCount
import kr.hhplus.be.server.infrastructure.dao.concert.QConcertScheduleEntity.concertScheduleEntity
import kr.hhplus.be.server.infrastructure.dao.concert.QConcertSeatEntity.concertSeatEntity
import kr.hhplus.be.server.infrastructure.dao.reservation.QReservationEntity.reservationEntity
import java.time.Clock
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class ReservationCustomRepositoryImpl(
    private val clock: Clock,
    private val queryFactory: JPAQueryFactory
) : ReservationCustomRepository {

    override fun findConcertReservationCountsByDate(date: LocalDate, size: Int): List<ConcertReservationCount> {
        return queryFactory
            .select(Projections.constructor(ConcertReservationCount::class.java, concertScheduleEntity.concertId, reservationEntity.count()))
            .from(concertScheduleEntity)
            .join(concertSeatEntity).on(concertSeatEntity.concertScheduleId.eq(concertScheduleEntity.id))
            .leftJoin(reservationEntity).on(
                reservationEntity.concertSeatId.eq(concertSeatEntity.id),
                reservationEntity.expiredAt.isNull.or(reservationEntity.expiredAt.after(LocalDateTime.now(clock))),
                reservationEntity.createdAt.between(date.atStartOfDay(), date.atTime(LocalTime.MAX))
            )
            .groupBy(concertScheduleEntity.concertId)
            .orderBy(reservationEntity.count().desc())
            .limit(size.toLong())
            .fetch()
    }
}