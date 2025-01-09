package kr.hhplus.be.server.infrastructure.dao.concert

import kr.hhplus.be.server.domain.model.concert.ConcertSeat
import org.springframework.data.jpa.repository.JpaRepository

interface ConcertSeatJpaRepository : JpaRepository<ConcertSeat, Long>, ConcertSeatCustomRepository