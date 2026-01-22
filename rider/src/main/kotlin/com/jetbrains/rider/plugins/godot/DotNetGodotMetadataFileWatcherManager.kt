package com.jetbrains.rider.plugins.godot

import com.intellij.openapi.application.EDT
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.intellij.openapi.vfs.VirtualFileManager
import com.jetbrains.rd.util.reactive.viewNotNull
import com.jetbrains.rd.util.threading.coroutines.launch
import com.jetbrains.rider.godot.community.GodotMetadataService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.nio.file.Paths

class DotNetGodotMetadataFileWatcherManager : ProjectActivity {
    override suspend fun execute(project: Project) {
        withContext(Dispatchers.EDT) {
            val godotDiscoverer = GodotProjectDiscoverer.getInstance(project)
            godotDiscoverer.godotDescriptor.viewNotNull(GodotProjectLifetimeService.getLifetime(project)) { lt, descriptor ->
                val mainProjectBasePath = Paths.get(descriptor.mainProjectBasePath)
                lt.launch(Dispatchers.IO){
                    VirtualFileManager.getInstance().addAsyncFileListener(lt.coroutineScope, DotNetGodotMetadataFileWatcher(project, mainProjectBasePath))
                }
                lt.launch(Dispatchers.IO) {
                    GodotMetadataService.getInstance(project).metadataChangeFlow.collect { event ->
                        if (event != null) {
                            val newPath = DotNetGodotMetadataFileWatcherUtil.getGodot4Path(mainProjectBasePath)
                            withContext(Dispatchers.EDT) {
                                newPath?.let { godotDiscoverer.godot4Path.set(it) }
                                godotDiscoverer.projectMetadataModificationSignal.fire(Unit)
                            }
                        }
                    }
                }
            }
        }
    }
}