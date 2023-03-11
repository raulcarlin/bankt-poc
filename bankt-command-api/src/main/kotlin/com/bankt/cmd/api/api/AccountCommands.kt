package com.bankt.cmd.api.api

import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.util.UUID

sealed class AccountCommand

data class OpenAccountCommand(
    @TargetAggregateIdentifier
    val id: String = UUID.randomUUID().toString(),
    val holder: String,
    val type: String,
    val openingBalance: Double
) : AccountCommand()

data class CloseAccountCommand(
    @TargetAggregateIdentifier
    val id: String
) : AccountCommand()

data class DepositFundsCommand(
    @TargetAggregateIdentifier
    val id: String,
    val amount: Double
) : AccountCommand()

data class WithdrawFundsCommand(
    @TargetAggregateIdentifier
    val id: String,
    val amount: Double
) : AccountCommand()
