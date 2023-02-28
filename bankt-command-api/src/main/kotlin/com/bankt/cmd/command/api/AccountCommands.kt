package com.bankt.cmd.command.api

import com.bankt.core.command.BaseCommand
import com.bankt.core.dto.AccountType
import java.util.UUID

sealed class AccountCommand : BaseCommand()

data class OpenAccountCommand(
    override val id: String = UUID.randomUUID().toString(),
    val holder: String,
    val type: AccountType,
    val openingBalance: Double
) : AccountCommand()

data class CloseAccountCommand(
    override val id: String
) : AccountCommand()

data class DepositFundsCommand(
    override val id: String,
    val amount: Double
) : AccountCommand()

data class WithdrawFundsCommand(
    override val id: String,
    val amount: Double
) : AccountCommand()
