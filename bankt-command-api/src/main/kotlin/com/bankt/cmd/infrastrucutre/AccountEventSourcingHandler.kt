package com.bankt.cmd.infrastrucutre

import com.bankt.cmd.domain.AccountAggregate
import com.bankt.core.domain.AggregateRoot
import com.bankt.core.handler.EventSourcingHandler
import com.bankt.core.infrastructure.EventStore
import com.bankt.core.producer.EventProducer
import org.springframework.stereotype.Service

@Service
class AccountEventSourcingHandler(
    val eventStore: EventStore,
    val eventProducer: EventProducer
) : EventSourcingHandler<AccountAggregate> {

    override fun save(aggregate: AggregateRoot) {
        eventStore.saveEvents(
            aggregate.id,
            aggregate.getUncommittedChanges(),
            expectedVersion = aggregate.version
        )
    }

    override fun getById(id: String): AccountAggregate =
        eventStore.getEvents(id).let {
            AccountAggregate().apply {
                replaySettingVersion(it)
            }
        }

    override fun republishEvents() =
        eventStore.getAllEvents().forEach(eventProducer::produce)

}
