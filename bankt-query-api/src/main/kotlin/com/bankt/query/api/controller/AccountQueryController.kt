package com.bankt.query.api.controller

import com.bankt.query.domain.BankAccountDto
import com.bankt.query.domain.FindAllBankAccountsQuery
import com.bankt.query.domain.FindBankAccountByIdQuery
import org.axonframework.queryhandling.QueryGateway
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("api/v1/account")
class AccountQueryController(
    private val queryGateway: QueryGateway
) {

    @GetMapping("/{id}")
    fun findById(@PathVariable id: String): Mono<BankAccountDto> =
        Mono.from(queryGateway.streamingQuery(FindBankAccountByIdQuery(id), BankAccountDto::class.java))


    @GetMapping("/all")
    fun findAll(): Flux<BankAccountDto> =
        Flux.from(queryGateway.streamingQuery(FindAllBankAccountsQuery(),BankAccountDto::class.java))

}

