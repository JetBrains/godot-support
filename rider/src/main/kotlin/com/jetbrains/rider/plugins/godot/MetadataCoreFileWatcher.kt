package com.jetbrains.rider.plugins.godot

import com.intellij.openapi.application.EDT
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.rd.util.launchBackground
import com.intellij.openapi.rd.util.lifetime
import com.intellij.openapi.startup.ProjectActivity
import com.intellij.util.application
import com.jetbrains.rd.util.lifetime.isAlive
import com.jetbrains.rd.util.reactive.viewNotNull
import com.jetbrains.rd.util.reactive.whenTrue
import com.jetbrains.rider.projectView.solution
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.time.withTimeout
import kotlinx.coroutines.withContext
import java.io.File
import java.nio.file.*
import java.nio.file.StandardWatchEventKinds.ENTRY_CREATE
import java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY
import java.time.Duration
import kotlin.io.path.isDirectory


class MetadataCoreFileWatcher : ProjectActivity {

    object Util {
        fun getGodotPath(mainProjectBasePath: String): String? {
            val projectPath = File(mainProjectBasePath)
            val projectMetadataCfg = projectPath.resolve(cfgDir).resolve(cfgFileName)

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

    companion object {
        const val cfgDir = ".godot/editor"
        const val cfgFileName = "project_metadata.cfg"
        private val logger = Logger.getInstance(MetadataCoreFileWatcher::class.java)

    }

    override suspend fun execute(project: Project) {
        withContext(Dispatchers.EDT) {
            project.solution.isLoaded.whenTrue(project.lifetime) {l->
                val godotDiscoverer = GodotProjectDiscoverer.getInstance(project)
                godotDiscoverer.mainProjectBasePath.viewNotNull(l) { lt, mainProjectBasePath ->
                    lt.launchBackground {
                        val watchService: WatchService = FileSystems.getDefault().newWatchService()
                        val metaFileDir = File(mainProjectBasePath).resolve(cfgDir).toPath()

                        withTimeout(Duration.ofMinutes(5)) {
                            while (!(metaFileDir.isDirectory())) {
                                // wait for folder to appear
                                delay(3000)
                            }
                        }

                        if (!(metaFileDir.isDirectory()))
                            return@launchBackground

                        metaFileDir.register(watchService, ENTRY_CREATE, ENTRY_MODIFY)

                        lt.onTerminationIfAlive {
                            watchService.close() // releases watchService.take()
                        }

                        var key: WatchKey
                        try {
                            while (watchService.take().also { key = it } != null && lt.isAlive) {
                                for (event in key.pollEvents()) {
                                    val context = event.context() ?: continue
                                    if (context.toString() == cfgFileName) {
                                        logger.info("GodotCoreProjectDiscoverer.getInstance(project).godotPath.set()")
                                        val newPath = Util.getGodotPath(mainProjectBasePath) ?: continue
                                        logger.info("GodotCoreProjectDiscoverer.getInstance(project).godotPath.set($newPath)")
                                        application.invokeLater {
                                            logger.info("application.invokeLater GodotProjectDiscoverer.getInstance(project).godotPath.set($newPath)")
                                            GodotProjectDiscoverer.getInstance(project).godotCorePath.set(newPath)
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