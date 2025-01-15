package kr.hhplus.be.server.infrastructure.dao.queue

import com.querydsl.jpa.impl.JPAQueryFactory
import kr.hhplus.be.server.infrastructure.dao.queue.QQueueEntity.queueEntity
import java.time.Clock
import java.time.LocalDateTime

class QueueCustomRepositoryImpl(
    private val clock: Clock,
    private val queryFactory: JPAQueryFactory
) : QueueCustomRepository {

    override fun getNotExpiredWithOrder(count: Int): List<QueueEntity> {
        return queryFactory
            .select(queueEntity)
            .from(queueEntity)
            .where(queueEntity.expiredAt.after(LocalDateTime.now(clock)))
            .orderBy(queueEntity.id.asc())
            .limit(count.toLong())
            .fetch()
    }

    override fun findNotExpiredByToken(token: String): QueueEntity? {
        return queryFactory
            .select(queueEntity)
            .from(queueEntity)
            .where(
                queueEntity.token.eq(token),
                queueEntity.expiredAt.after(LocalDateTime.now(clock))
            )
            .fetchOne()
    }
}