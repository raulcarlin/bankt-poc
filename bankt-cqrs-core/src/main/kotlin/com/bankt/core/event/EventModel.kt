package com.bankt.core.event

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime
import java.util.UUID

@Document(collection = "eventStore")
data class EventModel(
    @Id
    val id: String = UUID.randomUUID().toString(),
    val timeStamp: LocalDateTime,
    val aggregateIdentifier: String,
    val aggregateType: String,
    val version: Long = 0,
    val eventType: String,
    val eventData: BaseEvent,
)
