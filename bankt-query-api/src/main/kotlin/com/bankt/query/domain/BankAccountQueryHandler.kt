package com.bankt.query.domain

import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Component
class BankAccountQueryHandler(
    private val bankAccountRepository: BankAccountRepository
) {

    @QueryHandler
    fun findAll(query: FindAllBankAccountsQuery): Flux<BankAccountDto> =
        bankAccountRepository.findAll().map {
            BankAccountDto(
                holder = it.holder,
                type = it.type,
                availableBalance = it.balance.div(100.0)
            )
        }

    @QueryHandler
    fun findById(query: FindBankAccountByIdQuery): Mono<BankAccountDto> =
        bankAccountRepository.findById(query.id).map {
            BankAccountDto(
                holder = it.holder,
                type = it.type,
                availableBalance = it.balance.div(100.0)
            )
        }
}


data class BankAccountDto(
    val holder: String,
    val type: String,
    val availableBalance: Double
)
