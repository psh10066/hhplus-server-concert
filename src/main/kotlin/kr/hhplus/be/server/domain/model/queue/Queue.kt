package kr.hhplus.be.server.domain.model.queue

import java.util.*

class Queue(
    val userUuid: UUID,
    val token: String,
) {

    companion object {
        fun create(userUuid: UUID): Queue {
            return Queue(
                userUuid = userUuid,
                token = "$userUuid|${UUID.randomUUID()}",
            )
        }

        fun parseUserUuid(token: String): UUID {
            return UUID.fromString(token.split("|")[0])
        }

        fun of(token: String): Queue {
            return Queue(
                userUuid = parseUserUuid(token),
                token = token,
            )
        }
    }
}