package com.jetbrains.rider.godot.community

import com.intellij.openapi.project.Project

// TODO: Allows us to move the LSP without disrupting the DAP. Remove after the DAP is moved as well
interface LspRunningStatusProvider {
    fun isLspRunning(project: Project): Boolean
    fun restartLsp(project: Project)
}