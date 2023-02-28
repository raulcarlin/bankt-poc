package com.bankt.query.infrastructure.handler

import com.bankt.core.event.AccountClosedEvent
import com.bankt.core.event.AccountEvent
import com.bankt.core.event.AccountOpenedEvent
import com.bankt.core.event.FundsDepositedEvent
import com.bankt.core.event.FundsWithrawnEvent
import com.bankt.core.event.BaseEvent
import com.bankt.core.handler.EventHandler
import com.bankt.core.logger
import com.bankt.query.domain.BankAccount
import com.bankt.query.domain.BankAccountRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import java.time.LocalDateTime

@Service
class AccountEventHandler(
    private val accountRepository: BankAccountRepository
) : EventHandler {

    private val log by logger()

    override fun on(event: BaseEvent) = with(event as AccountEvent) {
        when (this) {
            is AccountOpenedEvent -> acccountOpened(this)
            is AccountClosedEvent -> accountClosed(this)
            is FundsWithrawnEvent -> fundsWithdrawn(this)
            is FundsDepositedEvent -> fundsDeposited(this)
        }
    }

    fun acccountOpened(event: AccountOpenedEvent) {
        accountRepository.save(
            BankAccount(
                id = event.id,
                balance = event.openingBalance.times(100).toInt(),
                type = event.type,
                holder = event.holder,
                createdAt = event.createdAt,
                updatedAt = LocalDateTime.now(),
                lastEventVersion = event.version
            ).apply {
                initialPersist = true
            }
        ).toFuture().get()
    }

    fun accountClosed(event: AccountClosedEvent) {
        getAndValidate(event)
            .flatMap(accountRepository::delete)
            .doOnError {
                log.error("Error closing account {} - {}", event.id, it.message)
            }.toFuture().get()
    }

    fun fundsWithdrawn(event: FundsWithrawnEvent) {
        getAndValidate(event).flatMap {
            accountRepository.save(
                it.copy(
                    balance = it.balance - event.amount.times(100).toInt(),
                    lastEventVersion = event.version
                )
            )
        }.doOnError {
            log.error("Error withdrawing funds for  account {} - {}", event.id, it.message)
        }.toFuture().get()
    }

    fun fundsDeposited(event: FundsDepositedEvent) {
        getAndValidate(event).flatMap {
            accountRepository.save(
                it.copy(
                    balance = it.balance - event.amount.times(100).toInt(),
                    lastEventVersion = event.version
                )
            )
        }.doOnError {
            log.error("Error depositing funds for  account {} - {}", event.id, it.message)
        }.toFuture().get()
    }

    private fun getAndValidate(event: BaseEvent): Mono<BankAccount> =
        accountRepository.findById(event.id).doOnNext {
            if ((event.version - it.lastEventVersion) != 1L) {
                throw IllegalStateException(
                    "Concurrency violated for ${event.id}, last version is ${it.lastEventVersion}, new event is ${event.version}"
                )
            }
        }

}
