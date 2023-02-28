package com.bankt.cmd.command.api.controller

import com.bankt.cmd.domain.AccountAggregate
import com.bankt.core.handler.EventSourcingHandler
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class AdminController(
    private val eventSourcingHandler: EventSourcingHandler<AccountAggregate>
) {
    @PostMapping("admin/rebuild")
    fun rebuildDatabase() = eventSourcingHandler.republishEvents()
}
