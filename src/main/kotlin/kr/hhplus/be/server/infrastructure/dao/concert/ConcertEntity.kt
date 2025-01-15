package kr.hhplus.be.server.infrastructure.dao.concert

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import kr.hhplus.be.server.domain.model.concert.Concert
import kr.hhplus.be.server.infrastructure.dao.BaseEntity

@Entity
@Table(name = "concert")
class ConcertEntity(
    id: Long = 0,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    val price: Long,
) : BaseEntity(id) {

    fun toModel(): Concert {
        return Concert(
            id = id,
            name = name,
            price = price,
        )
    }

    companion object {
        fun from(concert: Concert): ConcertEntity {
            return ConcertEntity(
                id = concert.id,
                name = concert.name,
                price = concert.price,
            )
        }
    }
}