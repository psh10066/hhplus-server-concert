package kr.hhplus.be.server.infrastructure.dao.concert

import org.springframework.data.jpa.repository.JpaRepository

interface ConcertJpaRepository : JpaRepository<ConcertEntity, Long>