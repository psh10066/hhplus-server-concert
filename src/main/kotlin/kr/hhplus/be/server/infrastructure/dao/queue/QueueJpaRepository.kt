package kr.hhplus.be.server.infrastructure.dao.queue

import org.springframework.data.jpa.repository.JpaRepository

interface QueueJpaRepository : JpaRepository<QueueEntity, Long>, QueueCustomRepository