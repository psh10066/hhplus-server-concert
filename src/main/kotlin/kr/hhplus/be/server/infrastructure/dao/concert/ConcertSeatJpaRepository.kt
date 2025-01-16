package kr.hhplus.be.server.infrastructure.dao.concert

import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.transaction.annotation.Transactional

interface ConcertSeatJpaRepository : JpaRepository<ConcertSeatEntity, Long>, ConcertSeatCustomRepository {

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    fun findForUpdateById(id: Long): ConcertSeatEntity?
}