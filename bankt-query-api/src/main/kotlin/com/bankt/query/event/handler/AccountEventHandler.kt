package com.bankt.query.event.handler

import com.bankt.core.event.AccountClosedEvent
import com.bankt.core.event.AccountOpenedEvent
import com.bankt.core.event.FundsDepositedEvent
import com.bankt.core.event.FundsWithrawnEvent
import com.bankt.core.logger
import com.bankt.query.domain.BankAccount
import com.bankt.query.domain.BankAccountRepository
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.time.LocalDateTime

@Service
class AccountEventHandler(
    private val accountRepository: BankAccountRepository
) {

    private val log by logger()

    @EventHandler
    fun acccountOpened(event: AccountOpenedEvent) {
        accountRepository.save(
            BankAccount(
                id = event.id,
                balance = event.openingBalance.times(100).toInt(),
                type = event.type,
                holder = event.holder,
                createdAt = event.createdAt,
                updatedAt = LocalDateTime.now()
            ).apply {
                initialPersist = true
            }
        ).toFuture().get()
    }

    @EventHandler
    fun accountClosed(event: AccountClosedEvent) {
        getAndValidate(event.id)
            .flatMap(accountRepository::delete)
            .doOnError {
                log.error("Error closing account {} - {}", event.id, it.message)
            }.toFuture().get()
    }

    @EventHandler
    fun fundsWithdrawn(event: FundsWithrawnEvent) {
        getAndValidate(event.id).flatMap {
            accountRepository.save(
                it.copy(
                    balance = it.balance - event.amount.times(100).toInt()
                )
            )
        }.doOnError {
            log.error("Error withdrawing funds for  account {} - {}", event.id, it.message)
        }.toFuture().get()
    }

    @EventHandler
    fun fundsDeposited(event: FundsDepositedEvent) {
        getAndValidate(event.id).flatMap {
            accountRepository.save(
                it.copy(
                    balance = it.balance - event.amount.times(100).toInt()
                )
            )
        }.doOnError {
            log.error("Error depositing funds for  account {} - {}", event.id, it.message)
        }.toFuture().get()
    }

    private fun getAndValidate(id: String): Mono<BankAccount> =
        accountRepository.findById(id)

}
