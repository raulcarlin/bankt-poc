package com.bankt.query.api.controller

import com.bankt.core.dto.AccountType
import com.bankt.query.domain.BankAccountRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("api/v1/account")
class AccountQueryController(
    private val accountRepository: BankAccountRepository
) {

    @GetMapping("/{id}")
    fun findById(@PathVariable id: String): Mono<BankAccountDto> = accountRepository.findById(id).map {
        BankAccountDto(
            holder = it.holder,
            type = it.type,
            availableBalance = it.balance.div(100.0)
        )
    }

    @GetMapping("/all")
    fun findAll(@PathVariable id: String): Flux<BankAccountDto> = accountRepository.findAll().map {
        BankAccountDto(
            holder = it.holder,
            type = it.type,
            availableBalance = it.balance.div(100.0)
        )
    }
}

data class BankAccountDto(
    val holder: String,
    val type: AccountType,
    val availableBalance: Double
)
