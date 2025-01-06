package kr.hhplus.be.server.domain.service

import kr.hhplus.be.server.domain.model.user.UserRepository
import kr.hhplus.be.server.domain.model.user.dto.UserInfo
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository
) {

    fun getUserInfo(id: Long): UserInfo {
        val user = userRepository.getUserById(id)
        return UserInfo.of(user)
    }
}