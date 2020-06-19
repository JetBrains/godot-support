package com.jetbrains.rider.plugins.godot

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.SystemInfo
import java.io.File
import java.math.BigInteger
import java.nio.file.Paths
import java.security.MessageDigest

class GodotServer {
    companion object {
        // https://github.com/godotengine/godot-proposals/issues/555#issuecomment-595242973
        //Windows: %APPDATA%\Godot\projects\{PROJECT_NAME}_{MD5_OF_PROJECT_PATH}\
        //macOS: $XDG_DATA_HOME/Godot/projects/{PROJECT_NAME}_{MD5_OF_PROJECT_PATH}/ or $HOME/Library/Application Support/Godot/projects/{PROJECT_NAME}_{MD5_OF_PROJECT_PATH}/
        //Linux: $XDG_DATA_HOME/godot/projects/{PROJECT_NAME}_{MD5_OF_PROJECT_PATH}/ or $HOME/.local/share/godot/projects/{PROJECT_NAME}_{MD5_OF_PROJECT_PATH}/
        fun getGodotPath(project:Project):String {
            val projectsSettingsPath = if (SystemInfo.isMac)
            {
                val home = Paths.get(System.getenv("HOME"))
                home.resolve("Library/Application Support/Godot/projects")
            }
            else if (SystemInfo.isLinux) {
                val home = Paths.get(System.getenv("HOME"))
                home.resolve(".config/godot/projects")
            }
            else if (SystemInfo.isWindows) {
                val appData = Paths.get(System.getenv("APPDATA"))
                appData.resolve("Godot/projects")
            }
            else
                throw Exception("Unexpected OS.")

            val projectPath = project.basePath
            val md5 = projectPath?.md5()
            val projectSettingsPath = projectsSettingsPath.resolve("${File(projectPath).name}-$md5")
            val projectMetadataCfg = projectSettingsPath.resolve("project_metadata.cfg").toFile()

            if (projectMetadataCfg.exists()){
                val line = projectMetadataCfg.readLines().filter { it.startsWith("executable_path") }.singleOrNull()
                if (line != null)
                {
                    val path = line.substring("executable_path=\"".length, line.trimEnd().length - 1)
                    return path
                }
            }

            return getFromMonoMetadataPath(project)
        }

        private fun getFromMonoMetadataPath(project: Project): String {
            val basePath = project.basePath ?: return ""
            val metaFile = File(basePath,".mono/metadata/ide_server_meta.txt")
            if (!metaFile.exists())
                return ""
            val lines = metaFile.readLines()
            if (lines.count()<2)
                return ""

            return lines[1]
        }

        fun String.md5(): String {
            val md = MessageDigest.getInstance("MD5")
            return BigInteger(1, md.digest(toByteArray())).toString(16).padStart(32, '0')
        }
    }
}