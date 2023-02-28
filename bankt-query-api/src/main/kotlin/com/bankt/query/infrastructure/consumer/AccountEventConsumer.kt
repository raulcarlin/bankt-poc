package com.bankt.query.infrastructure.consumer

import com.bankt.core.event.AccountClosedEvent
import com.bankt.core.event.AccountOpenedEvent
import com.bankt.core.event.FundsDepositedEvent
import com.bankt.core.event.FundsWithrawnEvent
import com.bankt.core.handler.EventHandler
import org.springframework.kafka.annotation.DltHandler
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.annotation.RetryableTopic
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy
import org.springframework.kafka.support.Acknowledgment
import org.springframework.retry.annotation.Backoff
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service

@Component
class AccountEventConsumer(
    val handler: EventHandler
) {
    @RetryableTopic(
        backoff = Backoff(value = 3000L),
        attempts = "5",
        topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE)
    @KafkaListener(topics = ["OPEN_ACCOUNT"], groupId = "bankt-query")
    fun consume(event: AccountOpenedEvent, ack: Acknowledgment) {
        handler.on(event)
        ack.acknowledge()
    }

    @RetryableTopic(
        backoff = Backoff(value = 3000L),
        attempts = "5",
        topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE
    )
    @KafkaListener(topics = ["CLOSE_ACCOUNT"], groupId = "bankt-query")
    fun consume(event: AccountClosedEvent, ack: Acknowledgment) {
        handler.on(event)
        ack.acknowledge()
    }

    @RetryableTopic(
        backoff = Backoff(value = 3000L),
        attempts = "5",
        topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE)
    @KafkaListener(topics = ["WITHDRAW_FUNDS"], groupId = "bankt-query")
    fun consume(event: FundsWithrawnEvent, ack: Acknowledgment) {
        handler.on(event)
        ack.acknowledge()
    }

    @RetryableTopic(
        backoff = Backoff(value = 3000L),
        attempts = "5",
        topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE)
    @KafkaListener(topics = ["DEPOSIT_FUNDS"], groupId = "bankt-query")
    fun consume(event: FundsDepositedEvent, ack: Acknowledgment) {
        handler.on(event)
        ack.acknowledge()
    }
}
