package com.bankt.core.domain

import com.bankt.core.event.BaseEvent

abstract class AggregateRoot {

    var id: String = ""
    var version: Long = -1

    private val changes: MutableList<BaseEvent> = mutableListOf()

    fun raiseEvent(event: BaseEvent) {
        applyChanges(event, isNewEvent = true)
    }

    private fun applyChanges(event: BaseEvent, isNewEvent: Boolean) {
        apply(event).also {
            if (isNewEvent) changes.add(event)
        }
    }

    fun replayEvents(events: Iterable<BaseEvent>) {
        events.forEach { applyChanges(it, isNewEvent = false) }
    }

    fun replaySettingVersion(events: List<BaseEvent>) {
        replayEvents(events).also {
            if (events.isNotEmpty()) version = events.maxOf { it.version } ?: -1
        }
    }

    fun getUncommittedChanges(): List<BaseEvent> = changes

    fun markChangesAsCommitted() = changes.clear()

    abstract fun apply(event: BaseEvent)

}
