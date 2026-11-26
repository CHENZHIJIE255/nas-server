package com.max.nas.monitor

import com.max.nas.common.Messages
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController


@RestController
class StatusController {
    @GetMapping("/api/status")
    fun status(): Map<String, Any>{
        return mapOf(
            Messages.CPU_CORES to Runtime.getRuntime().availableProcessors(),
            Messages.JAVA_VERSION to System.getProperty("java.version"),
            Messages.MAX_MEMORY_MB to Runtime.getRuntime().maxMemory() / 1024 / 1024,
            Messages.MESSAGE to Messages.SERVER_RUNNING
        )
    }
}