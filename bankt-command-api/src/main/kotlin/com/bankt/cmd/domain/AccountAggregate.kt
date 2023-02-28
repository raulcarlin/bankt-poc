package com.bankt.cmd.domain

import com.bankt.cmd.command.api.OpenAccountCommand
import com.bankt.core.event.AccountEvent
import com.bankt.core.event.AccountClosedEvent
import com.bankt.core.event.FundsDepositedEvent
import com.bankt.core.event.FundsWithrawnEvent
import com.bankt.core.event.AccountOpenedEvent
import com.bankt.core.domain.AggregateRoot
import com.bankt.core.event.BaseEvent
import java.time.LocalDateTime

class AccountAggregate(
    private var balance: Double,
    private var active: Boolean,
) : AggregateRoot() {

    constructor() :  this(
        balance = 0.0,
        active = false
    )

    constructor(openCommand: OpenAccountCommand) : this(
        balance = 0.0,
        active = false
    ) {
        raiseEvent(
            AccountOpenedEvent(
                id = openCommand.id,
                openingBalance = openCommand.openingBalance,
                holder = openCommand.holder,
                type = openCommand.type,
                createdAt = LocalDateTime.now()
            )
        )
    }

    fun isActive() = active

    override fun apply(event: BaseEvent) {
        with(event as AccountEvent) {
            when (this) {
                is AccountOpenedEvent -> accountOpened(this)
                is AccountClosedEvent -> accountClosed(this)
                is FundsWithrawnEvent -> fundsWithdrawn(this)
                is FundsDepositedEvent -> fundsDeposited(this)
            }
        }
    }

    private fun accountOpened(event: AccountOpenedEvent) {
        this.id = event.id
        this.balance = event.openingBalance
        this.active = true
    }

    fun depositFunds(amount: Double) {
        require(active) { "Account is closed!" }
        require(amount > 0) { "Amount should be greater than 0!" }

        raiseEvent(FundsDepositedEvent(
            id = this.id,
            amount = amount
        ))
    }

    private fun fundsDeposited(event: FundsDepositedEvent) {
        this.balance += event.amount
    }

    fun withdrawFunds(amount: Double) {
        require(active) { "Account is closed!" }
        require(amount > 0 && balance > amount) { "Amount should be greater than 0 and smaller then balance!" }

        raiseEvent(FundsWithrawnEvent(
            id = this.id,
            amount = amount
        ))
    }

    private fun fundsWithdrawn(event: FundsWithrawnEvent) {
        this.balance -= event.amount
    }

    fun closeAccount() {
        require(active) { "Account is already closed!" }

        raiseEvent(AccountClosedEvent(
            id = this.id
        ))
    }

    private fun accountClosed(event: AccountClosedEvent) {
        this.active = false
    }
}
