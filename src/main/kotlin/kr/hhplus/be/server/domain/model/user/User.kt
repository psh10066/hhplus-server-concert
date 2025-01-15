package kr.hhplus.be.server.domain.model.user

import java.util.*

class User(
    val id: Long = 0,
    val uuid: UUID = UUID.randomUUID(),
    val name: String,
)