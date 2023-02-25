package com.bankt.command

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BanktCommandApiApplication

fun main(args: Array<String>) {
	runApplication<BanktCommandApiApplication>(*args)
}
