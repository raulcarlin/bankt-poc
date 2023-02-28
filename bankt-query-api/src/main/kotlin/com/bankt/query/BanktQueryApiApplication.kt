package com.bankt.query

import io.r2dbc.spi.ConnectionFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.core.io.ClassPathResource
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator
import org.springframework.stereotype.Component


@SpringBootApplication
class BanktQueryApiApplication

fun main(args: Array<String>) {
	runApplication<BanktQueryApiApplication>(*args)
}

@Component
class DatabaseInitializer {
	@Bean
	fun initializer(connectionFactory: ConnectionFactory?): ConnectionFactoryInitializer? {
		val initializer = ConnectionFactoryInitializer()
		initializer.setConnectionFactory(connectionFactory!!)
		initializer.setDatabasePopulator(ResourceDatabasePopulator(ClassPathResource("db/schema.sql")))
		return initializer
	}
}
