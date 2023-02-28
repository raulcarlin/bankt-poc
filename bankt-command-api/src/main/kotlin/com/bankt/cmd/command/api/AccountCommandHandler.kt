package com.bankt.cmd.command.api

import com.bankt.cmd.domain.AccountAggregate
import com.bankt.core.command.BaseCommand
import com.bankt.core.command.CommandHandler
import com.bankt.core.handler.EventSourcingHandler
import com.bankt.core.logger
import org.springframework.stereotype.Service


@Service
class AccountCommandHandler(
    private val eventSourcingHandler: EventSourcingHandler<AccountAggregate>
): CommandHandler {

    private val log by logger()

    override fun invoke(command: BaseCommand) = with(command as AccountCommand) {
        try {
            when(command) {
                is OpenAccountCommand -> handleOpenAccount(command)
                is DepositFundsCommand -> handleDepositFunds(command)
                is WithdrawFundsCommand -> handleWithdrawFunds(command)
                is CloseAccountCommand -> handleCloseAccount(command)
            }
        } catch(e: Exception) {
            log.error("Failed to execute {} with error {}",
                command.id, e.message
            )
            // TODO: Redrive/Retry/etc
        }
    }

    private fun handleOpenAccount(command: OpenAccountCommand) {
        val aggregate = AccountAggregate(command)
        eventSourcingHandler.save(aggregate)
    }

    private fun handleDepositFunds(command: DepositFundsCommand) {
        val aggregate = eventSourcingHandler.getById(command.id)
        aggregate.depositFunds(command.amount)
        eventSourcingHandler.save(aggregate)
    }

    private fun handleWithdrawFunds(command: WithdrawFundsCommand) {
        val aggregate = eventSourcingHandler.getById(command.id)
        aggregate.withdrawFunds(command.amount)
        eventSourcingHandler.save(aggregate)
    }

    private fun handleCloseAccount(command: CloseAccountCommand) {
        val aggregate = eventSourcingHandler.getById(command.id)
        aggregate.closeAccount()
        eventSourcingHandler.save(aggregate)
    }

}
