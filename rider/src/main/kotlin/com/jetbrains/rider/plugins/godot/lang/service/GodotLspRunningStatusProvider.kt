package com.jetbrains.rider.plugins.godot.lang.service

import com.intellij.openapi.project.Project
import com.intellij.platform.lsp.api.LspServerManager
import com.intellij.platform.lsp.api.LspServerState
import com.jetbrains.rider.godot.community.LspRunningStatusProvider

class GodotLspRunningStatusProvider : LspRunningStatusProvider {
    override fun isLspRunning(project: Project): Boolean {
        return isGodotLspRunning(project)
    }
}


fun isGodotLspRunning(project: Project): Boolean {
    val manager = LspServerManager.getInstance(project)
    val provider = manager.getServersForProvider(GodotLspServerSupportProvider::class.java)
    val isRunning = provider.any { it.state == LspServerState.Running }
    return isRunning
}