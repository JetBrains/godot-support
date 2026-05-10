package com.jetbrains.rider.godot.community

import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.jetbrains.rd.util.lifetime.SequentialLifetimes
import com.jetbrains.rider.godot.community.utils.GodotCommunityUtil

class GodotMetadataFileWatcherManager : ProjectActivity {
    override suspend fun execute(project: Project) {
        val metadataService = GodotMetadataService.getInstance(project)
        val watcherLifetimes = SequentialLifetimes(metadataService.serviceLifetime.createNested())

        GodotCommunityUtil.getGodotProjectBasePathFlow(project).collect { basePath ->
            val lt = watcherLifetimes.next()
            if (basePath != null) {
                metadataService.watchBasePath(basePath, lt.lifetime)
            }
        }
    }
}
