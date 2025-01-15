package kr.hhplus.be.server.infrastructure.dao.payment

import org.springframework.data.jpa.repository.JpaRepository

interface PaymentHistoryJpaRepository : JpaRepository<PaymentHistoryEntity, Long>