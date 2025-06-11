package com.jetbrains.rider.godot.community

import com.intellij.openapi.project.Project
import com.jetbrains.rd.util.reactive.IOptProperty
import kotlinx.coroutines.Deferred

interface ProjectInfoProvider {
    fun isGodotProject(project: Project): Deferred<Boolean>
    fun getGodotPath(project: Project): IOptProperty<String>
    fun getMainProjectBasePathProperty(project: Project): IOptProperty<String>?
}