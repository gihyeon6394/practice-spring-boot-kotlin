package com.practice.kopring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
class PracticeKopringApplication

fun main(args: Array<String>) {
	runApplication<PracticeKopringApplication>(*args)
}
