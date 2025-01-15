package kr.hhplus.be.server.infrastructure.dao.user

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import kr.hhplus.be.server.domain.model.user.User
import kr.hhplus.be.server.infrastructure.dao.BaseEntity
import java.util.*

@Entity
@Table(name = "user")
class UserEntity(
    id: Long = 0,

    @Column(nullable = false, unique = true)
    val uuid: UUID = UUID.randomUUID(),

    @Column(nullable = false)
    val name: String,
) : BaseEntity(id) {

    fun toModel(): User {
        return User(
            id = id,
            uuid = uuid,
            name = name
        )
    }

    companion object {
        fun from(user: User): UserEntity {
            return UserEntity(
                id = user.id,
                uuid = user.uuid,
                name = user.name
            )
        }
    }
}