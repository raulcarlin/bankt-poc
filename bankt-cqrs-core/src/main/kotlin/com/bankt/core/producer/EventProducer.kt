package com.bankt.core.producer

import com.bankt.core.event.BaseEvent

interface EventProducer {
    fun produce(event: BaseEvent)
}
