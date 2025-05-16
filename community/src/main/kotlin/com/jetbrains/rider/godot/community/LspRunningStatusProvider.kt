package com.jetbrains.rider.godot.community

import com.intellij.openapi.project.Project

interface LspRunningStatusProvider {
    fun isLspRunning(project: Project): Boolean
}

