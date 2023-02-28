package com.bankt.cmd.infrastrucutre

import com.bankt.core.event.AccountClosedEvent
import com.bankt.core.event.AccountEvent
import com.bankt.core.event.AccountOpenedEvent
import com.bankt.core.event.BaseEvent
import com.bankt.core.event.FundsDepositedEvent
import com.bankt.core.event.FundsWithrawnEvent
import com.bankt.core.logger
import com.bankt.core.producer.EventProducer
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class AccountEventProducer(
    private val kafkaTemplate: KafkaTemplate<String, Any>
) : EventProducer {

    private val log by logger()

    override fun produce(event: BaseEvent) {
        val topicName = getTopicFor(event as AccountEvent)
        log.info("Kafka producer sending {} to {}", event, topicName)
        kafkaTemplate.send(topicName, event)
    }

    private fun getTopicFor(event: AccountEvent): String =
        when(event) {
            is AccountOpenedEvent -> "OPEN_ACCOUNT"
            is AccountClosedEvent -> "CLOSE_ACCOUNT"
            is FundsWithrawnEvent -> "WITHDRAW_FUNDS"
            is FundsDepositedEvent -> "DEPOSIT_FUNDS"
        }

}
