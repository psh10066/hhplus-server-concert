package kr.hhplus.be.server.domain.model.user

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import kr.hhplus.be.server.infrastructure.dao.BaseEntity
import java.util.*

@Entity
@Table(name = "user")
class User(
    @Column(nullable = false, unique = true)
    val uuid: UUID = UUID.randomUUID(),

    @Column(nullable = false)
    val name: String,
) : BaseEntity()