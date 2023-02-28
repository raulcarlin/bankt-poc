package com.bankt.cmd.infrastrucutre

import com.bankt.cmd.command.api.AccountCommand
import com.bankt.cmd.command.api.AccountCommandHandler
import com.bankt.cmd.command.api.CloseAccountCommand
import com.bankt.cmd.command.api.DepositFundsCommand
import com.bankt.cmd.command.api.OpenAccountCommand
import com.bankt.cmd.command.api.WithdrawFundsCommand
import com.bankt.core.command.BaseCommand
import com.bankt.core.infrastructure.CommandDispatcher
import com.bankt.core.command.CommandHandler
import com.bankt.core.logger
import jakarta.annotation.PostConstruct
import org.springframework.boot.context.event.ApplicationContextInitializedEvent
import org.springframework.boot.context.event.ApplicationPreparedEvent
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.event.ApplicationContextEvent
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service

@Service
class AccountCommandDispatcher(
    val accountCommandHandlers: List<AccountCommandHandler>
) : CommandDispatcher {

    private val log by logger()
    private val routes = mutableMapOf<Class<out BaseCommand>, MutableSet<CommandHandler>>()

    init {
        accountCommandHandlers.forEach {
            registerHandler(handler = it, type = AccountCommand::class.java)
        }
    }

    override fun <T : BaseCommand> registerHandler(type: Class<T>, handler: CommandHandler) {
        routes.computeIfAbsent(type) {
            mutableSetOf()
        }?.add(handler)
    }

    override fun send(command: BaseCommand) {
        with(routes.filterKeys { it.isAssignableFrom(command::class.java) }.values.flatten()) {
            requireNotNull(this != null && this.isNotEmpty()) { "No command handler found for $command" }
            this.forEach {
                log.info(
                    "Handling command {} for id {} with {}",
                    command::class.java.canonicalName,
                    command.id,
                    it::class.java.canonicalName
                )
                it(command)
            }
        }
    }

}
