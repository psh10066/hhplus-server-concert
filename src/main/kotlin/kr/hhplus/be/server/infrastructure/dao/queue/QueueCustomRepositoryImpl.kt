package kr.hhplus.be.server.infrastructure.dao.queue

import com.querydsl.jpa.impl.JPAQueryFactory
import kr.hhplus.be.server.domain.model.queue.QQueue.queue
import kr.hhplus.be.server.domain.model.queue.Queue
import java.time.Clock
import java.time.LocalDateTime

class QueueCustomRepositoryImpl(
    private val clock: Clock,
    private val queryFactory: JPAQueryFactory
) : QueueCustomRepository {

    override fun getNotExpiredWithOrder(count: Int): List<Queue> {
        return queryFactory
            .select(queue)
            .from(queue)
            .where(queue.expiredAt.after(LocalDateTime.now(clock)))
            .orderBy(queue.id.asc())
            .limit(count.toLong())
            .fetch()
    }

    override fun findNotExpiredByToken(token: String): Queue? {
        return queryFactory
            .select(queue)
            .from(queue)
            .where(
                queue.token.eq(token),
                queue.expiredAt.after(LocalDateTime.now(clock))
            )
            .fetchOne()
    }
}