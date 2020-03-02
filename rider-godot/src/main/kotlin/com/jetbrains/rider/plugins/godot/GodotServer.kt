package com.jetbrains.rider.plugins.godot

import com.intellij.openapi.project.Project
import com.jetbrains.rider.test.framework.combine
import java.io.File

class GodotServer {
    companion object {
        fun getPath(project: Project): String {
            val metaFile = File(project.basePath).combine(".mono\\metadata\\ide_server_meta.txt")
            if (!metaFile.exists())
                return ""
            val lines = metaFile.readLines()
            if (lines.count()<2)
                return ""

            return lines[1]
        }
    }
}