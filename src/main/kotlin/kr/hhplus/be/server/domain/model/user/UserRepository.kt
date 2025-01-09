package kr.hhplus.be.server.domain.model.user

import java.util.UUID

interface UserRepository {

    fun getUserById(id: Long): User

    fun getUserByUuid(uuid: UUID): User
}