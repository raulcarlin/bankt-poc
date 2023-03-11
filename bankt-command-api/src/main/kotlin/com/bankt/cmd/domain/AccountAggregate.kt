package com.bankt.cmd.domain

import com.bankt.cmd.api.api.CloseAccountCommand
import com.bankt.cmd.api.api.DepositFundsCommand
import com.bankt.cmd.api.api.OpenAccountCommand
import com.bankt.cmd.api.api.WithdrawFundsCommand
import com.bankt.core.event.AccountClosedEvent
import com.bankt.core.event.AccountOpenedEvent
import com.bankt.core.event.FundsDepositedEvent
import com.bankt.core.event.FundsWithrawnEvent
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate
import java.time.LocalDateTime

@Aggregate
class AccountAggregate(
    @AggregateIdentifier
    var id: String = "",
    var balance: Double = 0.0,
    var active: Boolean = false
) {

    constructor() : this(
        id = "",
        balance = 0.0,
        active = false
    )

    @CommandHandler
    constructor(openCommand: OpenAccountCommand) : this(
        id = openCommand.id,
        balance = openCommand.openingBalance,
        active = true
    ) {
        AggregateLifecycle.apply(
            AccountOpenedEvent(
                id = openCommand.id,
                openingBalance = openCommand.openingBalance,
                holder = openCommand.holder,
                type = openCommand.type,
                createdAt = LocalDateTime.now()
            )
        )
    }

    @CommandHandler
    fun handle(command: DepositFundsCommand) {
        depositFunds(command.amount)
    }

    @CommandHandler
    fun handle(command: WithdrawFundsCommand) {
        withdrawFunds(command.amount)
    }

    @CommandHandler
    fun handle(command: CloseAccountCommand) {
        closeAccount()
    }

    fun isActive() = active

    @EventSourcingHandler
    fun on(event: AccountOpenedEvent) {
        this.id = event.id
        this.balance = event.openingBalance
        this.active = true
    }

    fun depositFunds(amount: Double) {
        require(active) { "Account is closed!" }
        require(amount > 0) { "Amount should be greater than 0!" }

        AggregateLifecycle.apply(FundsDepositedEvent(
            id = this.id,
            amount = amount
        ))
    }

    @EventSourcingHandler
    fun on(event: FundsDepositedEvent) {
        this.balance += event.amount
    }

    fun withdrawFunds(amount: Double) {
        require(active) { "Account is closed!" }
        require(amount > 0 && balance > amount) { "Amount should be greater than 0 and smaller then balance!" }

        AggregateLifecycle.apply(FundsWithrawnEvent(
            id = this.id,
            amount = amount
        ))
    }

    @EventSourcingHandler
    fun on(event: FundsWithrawnEvent) {
        this.balance -= event.amount
    }

    fun closeAccount() {
        require(active) { "Account is already closed!" }

        AggregateLifecycle.apply(AccountClosedEvent(
            id = this.id
        ))
    }

    @EventSourcingHandler
    fun on(event: AccountClosedEvent) {
        this.active = false
    }
}
