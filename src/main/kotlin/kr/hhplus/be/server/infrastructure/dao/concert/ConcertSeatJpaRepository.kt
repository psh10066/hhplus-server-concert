package kr.hhplus.be.server.infrastructure.dao.concert

import org.springframework.data.jpa.repository.JpaRepository

interface ConcertSeatJpaRepository : JpaRepository<ConcertSeatEntity, Long>, ConcertSeatCustomRepository