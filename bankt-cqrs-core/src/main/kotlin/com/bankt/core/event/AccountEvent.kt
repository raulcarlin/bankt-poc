package com.bankt.core.event

import com.bankt.core.dto.AccountType
import java.time.LocalDateTime

sealed class AccountEvent : BaseEvent()

data class AccountOpenedEvent(
    override val id: String,
    val holder: String,
    val type: AccountType,
    val openingBalance: Double,
    val createdAt: LocalDateTime
) : AccountEvent()

class AccountClosedEvent(
    override val id: String
) : AccountEvent()

data class FundsDepositedEvent(
    override val id: String,
    val amount: Double
) : AccountEvent()

data class FundsWithrawnEvent(
    override val id: String,
    val amount: Double
) : AccountEvent()
