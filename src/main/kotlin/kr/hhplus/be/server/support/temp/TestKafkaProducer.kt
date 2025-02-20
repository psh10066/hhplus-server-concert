package kr.hhplus.be.server.support.temp

import org.springframework.boot.CommandLineRunner
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class TestKafkaProducer(
    private val kafkaTemplate: KafkaTemplate<String, String>
) : CommandLineRunner {

    override fun run(vararg args: String?) {
        kafkaTemplate.send("test", "test message!")
    }
}