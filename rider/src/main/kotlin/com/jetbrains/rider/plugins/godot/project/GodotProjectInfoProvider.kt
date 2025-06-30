package com.jetbrains.rider.plugins.godot.project

import com.intellij.openapi.project.Project
import com.jetbrains.rider.godot.community.ProjectInfoProvider
import com.jetbrains.rider.plugins.godot.GodotProjectDiscoverer
import kotlinx.coroutines.Deferred
import java.nio.file.Path

class GodotProjectInfoProvider : ProjectInfoProvider {

    override fun isGodotProject(project: Project): Deferred<Boolean> {
        return GodotProjectDiscoverer.getInstance(project).isGodotProject
    }

    override fun getMainProjectBasePathProperty(project: Project): Deferred<Path> {
        return GodotProjectDiscoverer.getInstance(project).mainProjectBasePath
    }
}