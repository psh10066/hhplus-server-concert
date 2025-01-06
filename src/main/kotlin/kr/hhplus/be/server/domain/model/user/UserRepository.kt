package kr.hhplus.be.server.domain.model.user

interface UserRepository {

    fun getUserById(id: Long): User
}