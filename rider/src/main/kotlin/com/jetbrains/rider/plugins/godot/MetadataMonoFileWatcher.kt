package com.jetbrains.rider.plugins.godot

import com.intellij.openapi.application.EDT
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.rd.util.lifetime
import com.intellij.openapi.startup.ProjectActivity
import com.intellij.openapi.util.SystemInfo
import com.jetbrains.rd.util.lifetime.isAlive
import com.jetbrains.rd.util.reactive.viewNotNull
import com.jetbrains.rd.util.reactive.whenTrue
import com.jetbrains.rd.util.threading.coroutines.launch
import com.jetbrains.rider.projectView.solution
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.math.BigInteger
import java.nio.file.*
import java.nio.file.StandardWatchEventKinds.ENTRY_CREATE
import java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY
import java.security.MessageDigest
import kotlin.io.path.isDirectory


class MetadataMonoFileWatcher : ProjectActivity {

    object Util {
        // https://github.com/godotengine/godot-proposals/issues/555#issuecomment-595242973
        //Windows: %APPDATA%\Godot\projects\{PROJECT_NAME}_{MD5_OF_PROJECT_PATH}\
        //macOS: $XDG_DATA_HOME/Godot/projects/{PROJECT_NAME}_{MD5_OF_PROJECT_PATH}/ or $HOME/Library/Application Support/Godot/projects/{PROJECT_NAME}_{MD5_OF_PROJECT_PATH}/
        //Linux: $XDG_DATA_HOME/godot/projects/{PROJECT_NAME}_{MD5_OF_PROJECT_PATH}/ or $HOME/.local/share/godot/projects/{PROJECT_NAME}_{MD5_OF_PROJECT_PATH}/
        fun getGodotPath(projectPath: File): String? {
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

            val md5 = projectPath.path.md5()
            val projectSettingsPath = projectsSettingsPath.resolve("${projectPath.toPath().fileName}-$md5")
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

        fun getFromMonoMetadataPath(basePath: File): String? {
            var metaFile = basePath.resolve(metaFileDir).resolve(metaFileName)
            if (!metaFile.exists())
                metaFile = basePath.resolve(metaFileDir).resolve(oldMetaFileName)
            if (!metaFile.exists()) return null

            val lines = metaFile.readLines()
            if (lines.count() < 2)
                return null

            if (Paths.get(lines[1]).toFile().exists())
                return lines[1]
            return null
        }
    }

    companion object {
        const val metaFileDir = ".mono/metadata"
        const val metaFileName = "ide_messaging_meta.txt"
        const val oldMetaFileName = "ide_server_meta.txt"
        private val logger = Logger.getInstance(MetadataMonoFileWatcher::class.java)

    }

    override suspend fun execute(project: Project) {
        withContext(Dispatchers.EDT) {
            project.solution.isLoaded.whenTrue(project.lifetime) { l ->
                val godotDiscoverer = GodotProjectDiscoverer.getInstance(project)
                godotDiscoverer.godotDescriptor.viewNotNull(l) { lt, descriptor ->
                    if (descriptor.isPureGdScriptProject) return@viewNotNull
                    val mainProjectBasePath = File(descriptor.mainProjectBasePath)
                    lt.launch(Dispatchers.IO) {
                        val watchService: WatchService = FileSystems.getDefault().newWatchService()
                        val metaFileDir = mainProjectBasePath.resolve(metaFileDir).toPath()

                        if (!(metaFileDir.isDirectory()))
                            return@launch

                        metaFileDir.register(watchService, ENTRY_CREATE, ENTRY_MODIFY)

                        lt.onTerminationIfAlive {
                            watchService.close() // releases watchService.take()
                        }

                        var key: WatchKey
                        try {
                            while (watchService.take().also { key = it } != null && lt.isAlive) {
                                for (event in key.pollEvents()) {
                                    val context = event.context() ?: continue
                                    if (context.toString() == metaFileName || context.toString() == oldMetaFileName) {
                                        logger.info("GodotProjectDiscoverer.getInstance(project).godotPath.set()")
                                        val newPath = Util.getFromMonoMetadataPath(mainProjectBasePath) ?: continue
                                        logger.info("GodotProjectDiscoverer.getInstance(project).godotPath.set($newPath)")
                                        withContext(Dispatchers.EDT) {
                                            logger.info("application.invokeLater GodotProjectDiscoverer.getInstance(project).godotPath.set($newPath)")
                                            GodotProjectDiscoverer.getInstance(project).godot3Path.set(newPath)
                                        }
                                    }
                                }
                                key.reset()
                            }
                        } catch (e: ClosedWatchServiceException) {
                        } // this is expected on `watchService.close()`

                    }
                }
            }
        }
    }
}