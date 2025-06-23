package com.jetbrains.rider.plugins.godot

import com.intellij.openapi.application.EDT
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.intellij.openapi.vfs.VirtualFileManager
import com.jetbrains.rd.util.reactive.viewNotNull
import com.jetbrains.rd.util.threading.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.nio.file.Paths

class GodotMetadataFileWatcherManager : ProjectActivity {
    override suspend fun execute(project: Project) {
        withContext(Dispatchers.EDT) {
            val godotDiscoverer = GodotProjectDiscoverer.getInstance(project)
            godotDiscoverer.godotDescriptor.viewNotNull(GodotProjectLifetimeService.getLifetime(project)) { lt, descriptor ->
                lt.launch(Dispatchers.IO){
                    val mainProjectBasePath = Paths.get(descriptor.mainProjectBasePath)
                    VirtualFileManager.getInstance().addAsyncFileListener(lt.coroutineScope, GodotMetadataFileWatcher(project, mainProjectBasePath))
                }
            }
        }
    }
}