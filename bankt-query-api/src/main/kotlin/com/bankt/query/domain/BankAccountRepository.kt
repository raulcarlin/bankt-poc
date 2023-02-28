package com.bankt.query.domain

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface BankAccountRepository : ReactiveCrudRepository<BankAccount, String>
