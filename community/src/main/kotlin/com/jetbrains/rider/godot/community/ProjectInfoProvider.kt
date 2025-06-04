package com.jetbrains.rider.godot.community

import com.intellij.openapi.project.Project
import com.jetbrains.rd.util.reactive.IProperty

interface ProjectInfoProvider {
    fun isGodotProject(project: Project): Boolean
    fun getMainProjectBasePathProperty(project: Project): IProperty<String?>?
}