package com.jetbrains.rider.plugins.godot.project

import com.intellij.openapi.project.Project
import com.jetbrains.rd.util.reactive.IOptProperty
import com.jetbrains.rd.util.reactive.OptProperty
import com.jetbrains.rd.util.reactive.adviseNotNull
import com.jetbrains.rd.util.threading.coroutines.nextNotNullValueAsync
import com.jetbrains.rider.godot.community.ProjectInfoProvider
import com.jetbrains.rider.plugins.godot.GodotProjectDiscoverer
import com.jetbrains.rider.plugins.godot.GodotProjectLifetimeService
import kotlinx.coroutines.Deferred
import java.util.concurrent.ConcurrentHashMap

class GodotProjectInfoProvider : ProjectInfoProvider {
    companion object {
        // Cache to store one property per project to avoid creating multiple subscriptions
        private val projectBasePathPropertyCache = ConcurrentHashMap<Project, IOptProperty<String>>()
    }
    
    override fun isGodotProject(project: Project): Deferred<Boolean> {
        return GodotProjectDiscoverer.Companion.getInstance(project).isGodotProject.nextNotNullValueAsync(
          GodotProjectLifetimeService.Companion.getLifetime(project))
    }

    override fun getGodotPath(project: Project): IOptProperty<String> {
        return GodotProjectDiscoverer.Companion.getInstance(project).godotPath
    }

    override fun getMainProjectBasePathProperty(project: Project): IOptProperty<String> {
        return projectBasePathPropertyCache.computeIfAbsent(project) {
            OptProperty<String>().also { prop ->
                val lifetime = GodotProjectLifetimeService.Companion.getLifetime(project)
                GodotProjectDiscoverer.Companion.getInstance(project).godotDescriptor.adviseNotNull(lifetime){ prop.set(it.mainProjectBasePath) }
            }
        }
    }
}