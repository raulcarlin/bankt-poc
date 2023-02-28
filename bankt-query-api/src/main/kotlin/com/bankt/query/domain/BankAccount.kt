package com.bankt.query.domain

import com.bankt.core.dto.AccountType
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import kotlin.properties.Delegates

@Table("bank_accounts")
data class BankAccount(
    @get:JvmName("getIdValue")
    @Id val id: String,
    val holder: String,
    val type: AccountType,
    val balance: Int,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val lastEventVersion: Long
) : Persistable<String> {
    @Transient var initialPersist: Boolean = false

    override fun getId(): String? = id

    override fun isNew(): Boolean = initialPersist
}
