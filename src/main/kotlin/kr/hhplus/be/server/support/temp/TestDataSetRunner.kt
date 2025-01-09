package kr.hhplus.be.server.support.temp

import kr.hhplus.be.server.domain.model.concert.Concert
import kr.hhplus.be.server.domain.model.concert.ConcertSchedule
import kr.hhplus.be.server.domain.model.concert.ConcertSeat
import kr.hhplus.be.server.domain.model.user.User
import kr.hhplus.be.server.domain.model.user.UserWallet
import kr.hhplus.be.server.infrastructure.dao.concert.ConcertJpaRepository
import kr.hhplus.be.server.infrastructure.dao.concert.ConcertScheduleJpaRepository
import kr.hhplus.be.server.infrastructure.dao.concert.ConcertSeatJpaRepository
import kr.hhplus.be.server.infrastructure.dao.user.UserJpaRepository
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

        val user = userJpaRepository.save(User(name = "테스트"))
        userWalletJpaRepository.save(UserWallet(userId = user.id))

        for (i in 1..10) {
            val concert = concertJpaRepository.save(Concert(name = "${i}번 콘서트", price = 10000L * i))
            for (j in 1..3) {
                concertScheduleJpaRepository.save(ConcertSchedule(
                    concertId = concert.id,
                    startTime = LocalDate.now().plusDays(j.toLong()).atTime(10 + j, 0)
                ))
            }
            for (j in 1..50) {
                concertSeatJpaRepository.save(ConcertSeat(concertId = concert.id, seatNumber = j))
            }
        }
    }
}