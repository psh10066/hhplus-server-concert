package kr.hhplus.be.server.infrastructure.dao.user

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserJpaRepository : JpaRepository<UserEntity, Long> {

    fun findByUuid(uuid: UUID): UserEntity?
}