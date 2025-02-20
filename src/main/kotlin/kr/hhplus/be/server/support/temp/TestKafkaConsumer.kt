package kr.hhplus.be.server.support.temp

import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class TestKafkaConsumer {
    private val log = LoggerFactory.getLogger(javaClass)

    @KafkaListener(topics = ["test"], groupId = "test-group")
    fun consume(message: String) {
        log.info("Received message: $message")
    }
}