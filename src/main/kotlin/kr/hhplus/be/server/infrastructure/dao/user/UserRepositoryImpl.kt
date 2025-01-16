package kr.hhplus.be.server.infrastructure.dao.user

import kr.hhplus.be.server.domain.model.user.User
import kr.hhplus.be.server.domain.model.user.UserRepository
import kr.hhplus.be.server.support.error.CustomException
import kr.hhplus.be.server.support.error.ErrorType
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import java.util.*

@Component
class UserRepositoryImpl(
    private val userJpaRepository: UserJpaRepository
) : UserRepository {

    override fun getUserById(id: Long): User {
        return userJpaRepository.findByIdOrNull(id)?.toModel()
            ?: throw CustomException(ErrorType.USER_NOT_FOUND)
    }

    override fun getUserByUuid(uuid: UUID): User {
        return userJpaRepository.findByUuid(uuid)?.toModel()
            ?: throw CustomException(ErrorType.USER_NOT_FOUND)
    }
}