package com.bankt.core.infrastructure

import com.bankt.core.event.BaseEvent

interface EventStore {
    fun saveEvents(aggregateId: String, events: Iterable<BaseEvent>, expectedVersion: Long)
    fun getEvents(aggregateId: String): List<BaseEvent>
    fun getAllEvents(): List<BaseEvent>
}
