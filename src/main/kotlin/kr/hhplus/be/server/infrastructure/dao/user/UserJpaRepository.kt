package kr.hhplus.be.server.infrastructure.dao.user

import kr.hhplus.be.server.domain.model.user.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserJpaRepository : JpaRepository<User, Long> {

    fun findByUuid(uuid: UUID): User?
}