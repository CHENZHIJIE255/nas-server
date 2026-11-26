package com.max.nas.files

import com.max.nas.common.FileTool
import com.max.nas.common.Messages
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

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
    fun getFiles(@RequestParam(defaultValue = Messages.ROOT_PATH) path: String) : Map<String, Any> {

        val dir = fileTool.jailed(path)
        if (!dir.exists() || !dir.isDirectory) {
            throw ApiException(HttpStatus.NOT_FOUND, "NOT_FOUND", Messages.DIR_NOT_FOUND.format(path))
        }
        val entries = dir.listFiles()?.sortedWith(
            compareBy({ it.isFile }, { it.name.lowercase() })  // 目录在前，按名排序
        ) ?: emptyList()

        return mapOf(
            Messages.PATH to dir.canonicalPath,
            Messages.ENTRIES to entries.map {
                FileEntry(
                    name = it.name,
                    type = if (it.isDirectory) Messages.TYPE_DIR else Messages.TYPE_FILE,
                    size = if (it.isFile) it.length() else null,
                    modified = it.lastModified()
                )
            }
        )
    }

    @DeleteMapping
    fun delete(@RequestParam path: String): Map<String, Any> {
        if (path.isBlank()) {
            throw ApiException(HttpStatus.FORBIDDEN, "FORBIDDEN", Messages.ROOT_DELETE_FORBIDDEN)
        }
        val file = fileTool.jailed(path)
        if (!file.exists()) {
            throw ApiException(HttpStatus.NOT_FOUND, "NOT_FOUND", Messages.NOT_FOUND.format(path))
        }
        val ok = file.deleteRecursively()
        if (!ok) {
            throw ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "OPERATION_FAILED", Messages.DELETE_FAILED)
        }
        return mapOf(Messages.OK to true, Messages.DELETED to path)
    }

    @PostMapping("/upload")
    fun upload(
        @RequestParam(Messages.TYPE_FILE) file: MultipartFile,
        @RequestParam(defaultValue = Messages.ROOT_PATH) dir: String
    ): Map<String, Any> {
        val targetDir = fileTool.jailed(dir)
        if (!targetDir.exists() || !targetDir.isDirectory) {
            throw ApiException(HttpStatus.NOT_FOUND, "NOT_FOUND", Messages.UPLOAD_DIR_NOT_FOUND.format(dir))
        }
        val rawName = file.originalFilename ?: Messages.UNNAMED_FILE
        val safeName = rawName
            .replace('\\', '/')
            .substringAfterLast('/')
            .ifBlank { Messages.UNNAMED_FILE }
        val target = fileTool.jailed(if (dir.isBlank()) safeName else "$dir/$safeName")
        file.transferTo(target)
        return mapOf(Messages.OK to true, Messages.NAME to target.name, Messages.SIZE to target.length())
    }

    @PostMapping("/mkdir")
    fun mkdir(@RequestParam path: String): Map<String, Any> {
        val dir = fileTool.jailed(path)
        if (dir.exists()) {
            throw ApiException(HttpStatus.CONFLICT, "ALREADY_EXISTS", Messages.ALREADY_EXISTS.format(path))
        }
        if (!dir.mkdirs()) {
            throw ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "OPERATION_FAILED", Messages.CREATE_FAILED)
        }
        return mapOf(Messages.OK to true, Messages.CREATED to path)
    }
}