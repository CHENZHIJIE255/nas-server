package com.max.nas.files

import com.max.nas.common.FileTool
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.io.File

data class FileEntry(
    val name: String,
    val type: String,      // "dir" 或 "file"
    val size: Long?,       // 文件才有大小
    val modified: Long     // 最后修改时间戳
)

@RestController
@RequestMapping("/api/files")
class FileController(private val fileTool: FileTool) {
    @GetMapping
    fun getFiles(@RequestParam(defaultValue = "/") path: String) : Map<String, Any> {

        val dir = fileTool.jailed(path)
        if (!dir.exists() || !dir.isDirectory) {
            return mapOf("error" to "目录不存在: $path")
        }
        val entries = dir.listFiles()?.sortedWith(
            compareBy({ it.isFile }, { it.name.lowercase() })  // 目录在前，按名排序
        ) ?: emptyList()

        return mapOf(
            "path" to dir.canonicalPath,
            "entries" to entries.map {
                FileEntry(
                    name = it.name,
                    type = if (it.isDirectory) "dir" else "file",
                    size = if (it.isFile) it.length() else null,
                    modified = it.lastModified()
                )
            }
        )
    }

    @DeleteMapping
    fun delete(@RequestParam path: String): Map<String, Any> {
        if (path.isBlank()) {
            return mapOf("ok" to false, "error" to "禁止删除数据根目录")
        }
        val file = fileTool.jailed(path)
        if (!file.exists()) {
            return mapOf("ok" to false, "error" to "不存在: $path")
        }
        val ok = file.deleteRecursively()   // Kotlin 标准库：递归删除，文件文件夹通吃！
        return if (ok) mapOf("ok" to true, "deleted" to path)
        else mapOf("ok" to false, "error" to "删除失败")
    }

    @PostMapping("/upload")
    fun upload(
        @RequestParam("file") file: MultipartFile,
        @RequestParam(defaultValue = "/") dir: String
    ): Map<String, Any> {
        val targetDir = fileTool.jailed(dir)
        if (!targetDir.exists() || !targetDir.isDirectory) {
            return mapOf("ok" to false, "error" to "目标目录不存在: $dir")
        }
        val rawName = file.originalFilename ?: "unnamed"
        val safeName = rawName
            .replace('\\', '/')           // 反斜杠也当分隔符
            .substringAfterLast('/')      // 只取最后一段（干掉 ../ 和路径）
            .ifBlank { "unnamed" }
        val target = fileTool.jailed(if (dir.isBlank()) safeName else "$dir/$safeName")
        file.transferTo(target)   // 流式写盘，不整块进内存
        return mapOf("ok" to true, "name" to target.name, "size" to target.length())
    }

    @PostMapping("/mkdir")
    fun mkdir(@RequestParam path: String): Map<String, Any> {
        val dir = fileTool.jailed(path)
        if (dir.exists()) {
            return mapOf("ok" to false, "error" to "已存在: $path")
        }
        return if (dir.mkdirs()) {
            mapOf("ok" to true, "created" to path)
        } else {
            mapOf("ok" to false, "error" to "创建失败")
        }
    }
}