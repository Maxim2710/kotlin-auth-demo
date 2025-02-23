package com.kotlinauthdemo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KotlinAuthDemoApplication

fun main(args: Array<String>) {
	runApplication<KotlinAuthDemoApplication>(*args)
}
