package com.jetbrains.rider.plugins.godot

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.SystemInfo
import com.intellij.util.application
import com.intellij.util.io.exists
import com.intellij.util.io.isDirectory
import com.jetbrains.rd.util.lifetime.isAlive
import com.jetbrains.rd.util.reactive.whenTrue
import com.jetbrains.rdclient.util.idea.LifetimedProjectComponent
import com.jetbrains.rider.projectView.solution
import com.jetbrains.rider.projectView.solutionDirectory
import java.math.BigInteger
import java.nio.file.*
import java.nio.file.StandardWatchEventKinds.ENTRY_CREATE
import java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY
import java.security.MessageDigest
import kotlin.concurrent.thread
import kotlin.io.path.readLines


class MetadataMonoFileWatcher(project: Project) : LifetimedProjectComponent(project) {

    companion object {
        const val metaFileDir = ".mono/metadata"
        const val metaFileName = "ide_messaging_meta.txt"
        const val oldMetaFileName = "ide_server_meta.txt"
        private val logger = Logger.getInstance(MetadataMonoFileWatcher::class.java)

        // https://github.com/godotengine/godot-proposals/issues/555#issuecomment-595242973
        //Windows: %APPDATA%\Godot\projects\{PROJECT_NAME}_{MD5_OF_PROJECT_PATH}\
        //macOS: $XDG_DATA_HOME/Godot/projects/{PROJECT_NAME}_{MD5_OF_PROJECT_PATH}/ or $HOME/Library/Application Support/Godot/projects/{PROJECT_NAME}_{MD5_OF_PROJECT_PATH}/
        //Linux: $XDG_DATA_HOME/godot/projects/{PROJECT_NAME}_{MD5_OF_PROJECT_PATH}/ or $HOME/.local/share/godot/projects/{PROJECT_NAME}_{MD5_OF_PROJECT_PATH}/
        fun getGodotPath(project:Project):String? {
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

            val projectPath = project.basePath!!
            val md5 = projectPath.md5()
            val projectSettingsPath = projectsSettingsPath.resolve("${Paths.get(projectPath).fileName}-$md5")
            val projectMetadataCfg = projectSettingsPath.resolve("project_metadata.cfg").toFile()

            if (projectMetadataCfg.exists()){
                val line = projectMetadataCfg.readLines().singleOrNull { it.startsWith("executable_path=") }
                if (line != null)
                {
                    val path = line.substring("executable_path=\"".length, line.trimEnd().length - 1)
                    if (Paths.get(path).exists())
                        return path
                }
            }
            return null
        }

        private fun String.md5(): String {
            val md = MessageDigest.getInstance("MD5")
            return BigInteger(1, md.digest(toByteArray())).toString(16).padStart(32, '0')
        }

        fun getFromMonoMetadataPath(project: Project): String? {
            val basePath = project.basePath ?: return null
            var metaFile = Paths.get(basePath, metaFileDir, metaFileName)
            if (!metaFile.exists())
                metaFile = Paths.get(basePath, metaFileDir, oldMetaFileName)
            if (!metaFile.exists()) return null

            val lines = metaFile.readLines()
            if (lines.count()<2)
                return null

            if (Paths.get(lines[1]).exists())
                return lines[1]
            return null
        }
    }

    init {
        project.solution.isLoaded.whenTrue(componentLifetime) {l->
            val godotDiscoverer = GodotProjectDiscoverer.getInstance(project)
            godotDiscoverer.isGodotProject.whenTrue(l) { lt ->
                thread(name = "MetadataFileWatcher") {
                    val watchService: WatchService = FileSystems.getDefault().newWatchService()
                    val metaFileDir = project.solutionDirectory.resolve(metaFileDir).toPath()

                    if (!(metaFileDir.isDirectory()))
                        return@thread

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
                                    val newPath = getFromMonoMetadataPath(project) ?: continue
                                    logger.info("GodotProjectDiscoverer.getInstance(project).godotPath.set($newPath)")
                                    application.invokeLater {
                                        logger.info("application.invokeLater GodotProjectDiscoverer.getInstance(project).godotPath.set($newPath)")
                                        GodotProjectDiscoverer.getInstance(project).godotMonoPath.set(newPath)
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