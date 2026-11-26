package com.max.nas.common

import com.max.nas.monitor.ApiException
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import java.io.File

@Component
class FileTool(
    @Value("\${nas.data-root:./nas-data}") private val dataRoot: String
) {
    /** 把用户给的路径锁进数据根目录，防止 ../ 越狱 */
    fun jailed(path: String): File {
        val root = File(dataRoot).canonicalFile          // 真实根（解析软链接）
        val target = File(root, path).canonicalFile      // 用户路径 + 解析 ..
        if (target != root && !target.path.startsWith(root.path + File.separator)) {
            throw ApiException(HttpStatus.NOT_FOUND, "NOT_FOUND", Messages.NOT_FOUND.format(path))
        }
        return target
    }

}