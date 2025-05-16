package com.jetbrains.rider.godot.community

import com.intellij.openapi.project.Project

interface ProjectInfoProvider {
    fun isGodotProject(project: Project): Boolean
}