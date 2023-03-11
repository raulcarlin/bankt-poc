package com.bankt.core.event

import java.time.LocalDateTime

sealed class AccountEvent

data class AccountOpenedEvent(
    val id: String,
    val holder: String,
    val type: String,
    val openingBalance: Double,
    val createdAt: LocalDateTime
) : AccountEvent()

class AccountClosedEvent(
    val id: String
) : AccountEvent()

data class FundsDepositedEvent(
    val id: String,
    val amount: Double
) : AccountEvent()

data class FundsWithrawnEvent(
    val id: String,
    val amount: Double
) : AccountEvent()
