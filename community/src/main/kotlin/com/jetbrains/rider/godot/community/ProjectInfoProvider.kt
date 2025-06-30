package com.jetbrains.rider.godot.community

import com.intellij.openapi.project.Project
import kotlinx.coroutines.Deferred
import java.nio.file.Path

interface ProjectInfoProvider {
    fun isGodotProject(project: Project): Deferred<Boolean>
    fun getMainProjectBasePathProperty(project: Project): Deferred<Path>
}