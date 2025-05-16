package com.jetbrains.rider.plugins.godot.lang.service

import com.intellij.openapi.project.Project
import com.jetbrains.rider.godot.community.ProjectInfoProvider
import com.jetbrains.rider.plugins.godot.GodotProjectDiscoverer

class GodotProjectInfoProvider : ProjectInfoProvider {
    override fun isGodotProject(project: Project): Boolean {
        return GodotProjectDiscoverer.Companion.getInstance(project).godotPath.value != null
    }
}