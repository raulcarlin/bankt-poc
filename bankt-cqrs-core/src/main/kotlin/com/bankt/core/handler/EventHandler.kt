package com.bankt.core.handler

import com.bankt.core.event.BaseEvent

interface EventHandler {
    fun on(event: BaseEvent)
}
