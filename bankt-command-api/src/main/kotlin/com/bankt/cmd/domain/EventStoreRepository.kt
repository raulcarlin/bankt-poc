package com.bankt.cmd.domain

import com.bankt.core.event.EventModel
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
interface EventStoreRepository : ReactiveMongoRepository<EventModel, String> {
    fun findByAggregateIdentifier(identifier: String): Flux<EventModel>
}
