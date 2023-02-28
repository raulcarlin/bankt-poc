package com.bankt.cmd.infrastrucutre

import com.bankt.cmd.domain.AccountAggregate
import com.bankt.cmd.domain.EventStoreRepository
import com.bankt.core.event.BaseEvent
import com.bankt.core.event.EventModel
import com.bankt.core.infrastructure.EventStore
import com.bankt.core.logger
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.concurrent.atomic.AtomicLong

@Service
class AccountEventStore(
    private val eventStoreRepository: EventStoreRepository,
    private val accountEventProducer: AccountEventProducer
) : EventStore {

    private val log by logger()

    override fun saveEvents(aggregateId: String, events: Iterable<BaseEvent>, expectedVersion: Long) {
        log.info("Storing events for {}", aggregateId)
        eventStoreRepository.findByAggregateIdentifier(aggregateId)
            .collectList()
            .subscribe { eventStream ->
                if (expectedVersion != -1L
                    && eventStream.isNotEmpty()
                    && eventStream[eventStream.size - 1].version != expectedVersion
                ) {
                    throw IllegalStateException("Concurrency violated $expectedVersion!").also {
                        log.info(
                            "Concurrency violated for {} with expected version {}",
                            aggregateId,
                            expectedVersion
                        )
                    }
                }
                var version = expectedVersion
                events.forEach { event ->
                    val eventType = event::class.java.canonicalName
                    event.version = ++version
                    eventStoreRepository.save(
                        EventModel(
                            timeStamp = LocalDateTime.now(),
                            aggregateIdentifier = aggregateId,
                            aggregateType = AccountAggregate::class.java.canonicalName,
                            version = event.version,
                            eventType = eventType,
                            eventData = event
                        )
                    ).subscribe {
                        log.info("Publishn event {} for {}", eventType, aggregateId)
                        accountEventProducer.produce(event)
                    }
                }
            }
    }

    override fun getEvents(aggregateId: String): List<BaseEvent> =
        eventStoreRepository.findByAggregateIdentifier(aggregateId)
            .map {
                it.eventData
            }
            .collectList()
            .toFuture()
            .get()

    override fun getAllEvents(): List<BaseEvent> =
        eventStoreRepository.findAll().map { it.eventData }.collectList().toFuture().get()

}
