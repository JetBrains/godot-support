package com.jetbrains.rider.plugins.godot

import com.intellij.openapi.util.SystemInfo
import com.jetbrains.rider.plugins.godot.GodotMetadataFileWatcher.Companion.metadataRelPath
import com.jetbrains.rider.plugins.godot.GodotMetadataFileWatcher.Companion.oldMonoMetadataRelPath
import java.math.BigInteger
import java.nio.file.Path
import java.nio.file.Paths
import java.security.MessageDigest
import kotlin.io.path.exists
import kotlin.io.path.pathString
import kotlin.io.path.readLines
import kotlin.text.substring
import kotlin.text.trimEnd

object GodotMetadataFileWatcherUtil {
    // Godot 3 path utilities
    fun getFromMonoMetadataPath(basePath: Path): String? {
        val metaFile = basePath.resolve(oldMonoMetadataRelPath)
        if (!metaFile.exists()) return null

        val lines = metaFile.readLines()
        if (lines.count() < 2)
            return null

        if (Paths.get(lines[1]).toFile().exists())
            return lines[1]
        return null
    }

    // Godot 3 path utilities for user settings
    // https://github.com/godotengine/godot-proposals/issues/555#issuecomment-595242973
    //Windows: %APPDATA%\Godot\projects\{PROJECT_NAME}_{MD5_OF_PROJECT_PATH}\
    //macOS: $XDG_DATA_HOME/Godot/projects/{PROJECT_NAME}_{MD5_OF_PROJECT_PATH}/ or $HOME/Library/Application Support/Godot/projects/{PROJECT_NAME}_{MD5_OF_PROJECT_PATH}/
    //Linux: $XDG_DATA_HOME/godot/projects/{PROJECT_NAME}_{MD5_OF_PROJECT_PATH}/ or $HOME/.local/share/godot/projects/{PROJECT_NAME}_{MD5_OF_PROJECT_PATH}/
    fun getGodot3Path(projectPath: Path): String? {
        val projectsSettingsPath = if (SystemInfo.isMac) {
            val home = Paths.get(System.getenv("HOME"))
            home.resolve("Library/Application Support/Godot/projects")
        } else if (SystemInfo.isLinux) {
            val home = Paths.get(System.getenv("HOME"))
            home.resolve(".config/godot/projects")
        } else if (SystemInfo.isWindows) {
            val appData = Paths.get(System.getenv("APPDATA"))
            appData.resolve("Godot/projects")
        } else
            throw Exception("Unexpected OS.")

        val md5 = projectPath.pathString.md5()
        val projectSettingsPath = projectsSettingsPath.resolve("${projectPath.fileName}-$md5")
        val projectMetadataCfg = projectSettingsPath.resolve("project_metadata.cfg").toFile()

        if (projectMetadataCfg.exists()) {
            val line = projectMetadataCfg.readLines().singleOrNull { it.startsWith("executable_path=") }
            if (line != null) {
                val path = line.substring("executable_path=\"".length, line.trimEnd().length - 1)
                if (Paths.get(path).toFile().exists())
                    return path
            }
        }
        return null
    }

    private fun String.md5(): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(toByteArray())).toString(16).padStart(32, '0')
    }

    // Godot 4 path utilities
    fun getGodot4Path(projectPath: Path): String? {
        val projectMetadataCfg = projectPath.resolve(metadataRelPath)

        if (projectMetadataCfg.exists()) {
            val line = projectMetadataCfg.readLines().singleOrNull { it.startsWith("executable_path=") }
            if (line != null) {
                val path = line.substring("executable_path=\"".length, line.trimEnd().length - 1)
                if (Paths.get(path).toFile().exists())
                    return path
            }
        }
        return null
    }
}