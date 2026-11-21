package com.max.nas.monitor

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController


@RestController
class StatusController {
    @GetMapping("/api/status")
    fun status(): Map<String, Any>{
        return mapOf(
            "cpuCores" to Runtime.getRuntime().availableProcessors(),
            "javaVersion" to System.getProperty("java.version"),
            "maxMemoryMB" to Runtime.getRuntime().maxMemory() / 1024 / 1024,
            "message" to "NAS 服务器第一课，跑起来了！"
        )
    }
}