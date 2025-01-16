package kr.hhplus.be.server.support.temp

import kr.hhplus.be.server.infrastructure.dao.concert.*
import kr.hhplus.be.server.infrastructure.dao.user.UserEntity
import kr.hhplus.be.server.infrastructure.dao.user.UserJpaRepository
import kr.hhplus.be.server.infrastructure.dao.user.UserWalletEntity
import kr.hhplus.be.server.infrastructure.dao.user.UserWalletJpaRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class TestDataSetRunner(
    private val userJpaRepository: UserJpaRepository,
    private val userWalletJpaRepository: UserWalletJpaRepository,
    private val concertJpaRepository: ConcertJpaRepository,
    private val concertScheduleJpaRepository: ConcertScheduleJpaRepository,
    private val concertSeatJpaRepository: ConcertSeatJpaRepository,
) : CommandLineRunner {

    override fun run(vararg args: String?) {
        if (userJpaRepository.count() > 0) {
            return
        }

        val user = userJpaRepository.save(UserEntity(name = "테스트"))
        userWalletJpaRepository.save(UserWalletEntity(userId = user.id))

        for (i in 1..10) {
            val concert = concertJpaRepository.save(ConcertEntity(name = "${i}번 콘서트", price = 10000L * i))
            for (j in 1..3) {
                val concertSchedule = concertScheduleJpaRepository.save(
                    ConcertScheduleEntity(
                        concertId = concert.id,
                        startTime = LocalDate.now().plusDays(j.toLong()).atTime(10 + j, 0)
                    )
                )
                for (k in 1..50) {
                    concertSeatJpaRepository.save(ConcertSeatEntity(concertScheduleId = concertSchedule.id, seatNumber = k))
                }
            }
        }
    }
}