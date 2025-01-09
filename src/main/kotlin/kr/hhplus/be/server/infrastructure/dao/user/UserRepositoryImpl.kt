package kr.hhplus.be.server.infrastructure.dao.user

import kr.hhplus.be.server.domain.model.user.User
import kr.hhplus.be.server.domain.model.user.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import java.util.*

@Component
class UserRepositoryImpl(
    private val userJpaRepository: UserJpaRepository
) : UserRepository {

    override fun getUserById(id: Long): User {
        return userJpaRepository.findByIdOrNull(id)
            ?: throw IllegalStateException("존재하지 않는 유저입니다.")
    }

    override fun getUserByUuid(uuid: UUID): User {
        return userJpaRepository.findByUuid(uuid)
            ?: throw IllegalStateException("존재하지 않는 유저입니다.")
    }
}