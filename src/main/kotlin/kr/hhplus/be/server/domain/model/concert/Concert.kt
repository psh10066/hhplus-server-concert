package kr.hhplus.be.server.domain.model.concert

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import kr.hhplus.be.server.infrastructure.dao.BaseEntity

@Entity
@Table(name = "concert")
class Concert(
    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    val price: Long,
) : BaseEntity()