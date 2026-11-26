package com.max.nas.download

import com.max.nas.common.FileTool
import com.max.nas.common.Messages
import com.max.nas.monitor.ApiException
import org.springframework.core.io.FileSystemResource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/download")
class DownloadController(private val fileTool: FileTool) {
    @GetMapping
    fun download(@RequestParam path: String): ResponseEntity<FileSystemResource> {
        val file = fileTool.jailed(path)
        if (!file.exists() || !file.isFile) {
            throw ApiException(HttpStatus.NOT_FOUND, "NOT_FOUND", Messages.NOT_FOUND.format(path))
        }
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"${file.name}\"")
            .contentLength(file.length())
            .body(FileSystemResource(file))
    }


}