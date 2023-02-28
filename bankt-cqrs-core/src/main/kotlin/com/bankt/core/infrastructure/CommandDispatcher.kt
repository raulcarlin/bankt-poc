package com.bankt.core.infrastructure

import com.bankt.core.command.BaseCommand
import com.bankt.core.command.CommandHandler

interface CommandDispatcher {
    fun <T : BaseCommand> registerHandler(type: Class<T>, handler: CommandHandler)
    fun send(command: BaseCommand)
}
