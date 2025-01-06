package kr.hhplus.be.server.domain.model.user.dto

import kr.hhplus.be.server.domain.model.user.User
import java.util.*

data class UserInfo(
    val id: Long,
    val uuid: UUID,
    val name: String
) {
    companion object {
        fun of(user: User): UserInfo {
            return UserInfo(
                id = user.id,
                uuid = user.uuid,
                name = user.name
            )
        }
    }
}
