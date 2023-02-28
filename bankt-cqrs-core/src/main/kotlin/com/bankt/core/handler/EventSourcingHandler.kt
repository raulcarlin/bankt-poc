package com.bankt.core.handler

import com.bankt.core.domain.AggregateRoot

interface EventSourcingHandler<T> {
    fun save(aggregate: AggregateRoot)
    fun getById(id: String): T
    fun republishEvents()
}
