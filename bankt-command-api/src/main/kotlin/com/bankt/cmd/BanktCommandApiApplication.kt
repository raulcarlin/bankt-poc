package com.bankt.cmd

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BanktCommandApiApplication

fun main(args: Array<String>) {
	runApplication<BanktCommandApiApplication>(*args)
}
