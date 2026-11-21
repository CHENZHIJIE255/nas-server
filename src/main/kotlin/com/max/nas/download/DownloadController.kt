package com.max.nas.download

import com.max.nas.common.FileTool
import org.springframework.core.io.FileSystemResource
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController                      // ← "我是接口类"
@RequestMapping("/api/download")
class DownloadController(private val fileTool: FileTool) {
    @GetMapping
    fun download(@RequestParam path: String): ResponseEntity<FileSystemResource> {
        println(">>> 列表接口被调用了，path=$path")   // ← 加这行
        val file = fileTool.jailed(path)
        if (!file.exists() || !file.isFile) {
            return ResponseEntity.notFound().build()
        }
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"${file.name}\"")
            .contentLength(file.length())          // 告诉浏览器文件多大
            .body(FileSystemResource(file))        // 流式发送，不整块读进内存
    }


}