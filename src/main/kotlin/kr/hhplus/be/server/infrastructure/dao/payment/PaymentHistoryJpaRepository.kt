package kr.hhplus.be.server.infrastructure.dao.payment

import kr.hhplus.be.server.domain.model.payment.PaymentHistory
import org.springframework.data.jpa.repository.JpaRepository

interface PaymentHistoryJpaRepository : JpaRepository<PaymentHistory, Long>