package com.bankt.core.event

import com.bankt.core.message.Message

abstract class BaseEvent : Message() {
    var version: Long = 0
}
