package com.max.nas

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class NasServerApplication

fun main(args: Array<String>) {
    runApplication<NasServerApplication>(*args)
}
