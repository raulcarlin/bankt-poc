package com.bankt.cmd.configuration

import com.mongodb.ConnectionString
import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoClients
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories

@Configuration(proxyBeanMethods = false)
@EnableReactiveMongoRepositories(
    basePackages = ["com.bankt"]
)
class ReactiveMongoDbRepositoryConfiguration(
    @Value("\${spring.data.mongodb.url}")
    private val mongoUri: String
) : AbstractReactiveMongoConfiguration() {
    private val databaseName: String = ConnectionString(mongoUri).database!!

    @Bean
    override fun reactiveMongoClient(): MongoClient {
        return MongoClients.create(mongoUri)
    }

    override fun getDatabaseName(): String = databaseName
}
