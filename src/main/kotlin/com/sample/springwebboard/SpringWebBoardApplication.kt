package com.sample.springwebboard

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringWebBoardApplication

fun main(args: Array<String>) {
	runApplication<SpringWebBoardApplication>(*args)
}