package com.bankt.cmd.api.api.controller

import com.bankt.cmd.api.api.CloseAccountCommand
import com.bankt.cmd.api.api.DepositFundsCommand
import com.bankt.cmd.api.api.OpenAccountCommand
import com.bankt.cmd.api.api.WithdrawFundsCommand
import com.bankt.core.logger
import org.axonframework.commandhandling.callbacks.LoggingCallback
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("command/v1/account")
class AccountCommandController(
    private val commandGateway: CommandGateway
) {
    private val log by logger()

    @PostMapping
    fun openAccount(@RequestBody openAccountCommand: OpenAccountCommand): Mono<ResponseEntity<BaseResponse>> {
        log.info("Dispatching OPEN ACCOUNT command for id {}", openAccountCommand.id)
        commandGateway.send(openAccountCommand, LoggingCallback.INSTANCE)
        return buildResponse(openAccountCommand.id, "Account request creation dispatched")
    }

    @PutMapping("deposit")
    fun depositFunds(@RequestBody depositFundsCommand: DepositFundsCommand): Mono<ResponseEntity<BaseResponse>> {
        log.info(
            "Dispatching DEPOSIT command for id {} with amount {}",
            depositFundsCommand.id,
            depositFundsCommand.amount
        )
        commandGateway.send(depositFundsCommand, LoggingCallback.INSTANCE)
        return buildResponse(
            depositFundsCommand.id,
            "Funds deposit request with ${depositFundsCommand.amount} dispateched",
            code = HttpStatus.ACCEPTED
        )
    }

    @PutMapping("withdraw")
    fun withdrawFunds(@RequestBody withdrawFundsCommand: WithdrawFundsCommand): Mono<ResponseEntity<BaseResponse>> {
        log.info(
            "Dispatching WITHDRAW command for id {} with amount {}",
            withdrawFundsCommand.id,
            withdrawFundsCommand.amount
        )
        commandGateway.send(withdrawFundsCommand, LoggingCallback.INSTANCE)
        return buildResponse(
            withdrawFundsCommand.id,
            "Funds withdraw request with ${withdrawFundsCommand.amount} dispateched",
            code = HttpStatus.ACCEPTED
        )
    }

    @DeleteMapping
    fun closeAccount(@RequestBody closeAccountCommand: CloseAccountCommand): Mono<ResponseEntity<BaseResponse>> {
        log.info("Dispatching CLOSE ACCOUNT command for id {}", closeAccountCommand.id)
        commandGateway.send(closeAccountCommand, LoggingCallback.INSTANCE)
        return buildResponse(closeAccountCommand.id, "Account close request dispatched")
    }

    private fun buildResponse(
        id: String,
        message: String,
        code: HttpStatus = HttpStatus.OK
    ): Mono<ResponseEntity<BaseResponse>> =
        Mono.just(ResponseEntity<BaseResponse>(BaseResponse(id, message), code))
}

data class BaseResponse(
    val id: String,
    val message: String
)
