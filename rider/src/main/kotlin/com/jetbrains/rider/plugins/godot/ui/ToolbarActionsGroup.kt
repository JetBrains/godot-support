package com.jetbrains.rider.plugins.godot.ui

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.actionSystem.ex.TooltipDescriptionProvider
import com.intellij.openapi.project.DumbAware
import com.jetbrains.rd.util.reactive.valueOrDefault
import com.jetbrains.rider.model.godot.frontendBackend.GodotEditorState
import com.jetbrains.rider.plugins.godot.FrontendBackendHost
import com.jetbrains.rider.plugins.godot.GodotIcons
import com.jetbrains.rider.plugins.godot.GodotProjectDiscoverer

class ToolbarActionsGroup : DefaultActionGroup(), DumbAware, TooltipDescriptionProvider {
    override fun update(e: AnActionEvent) {
        val project = e.project
        if (project == null) {
            e.presentation.isVisible = false
            return
        }

        e.presentation.isVisible = GodotProjectDiscoverer.getInstance(project).mainProjectBasePath.value != null

        if (e.presentation.isVisible) {
            e.presentation.isEnabled = true
            val host = FrontendBackendHost.getInstance(project)
            e.presentation.icon = GodotIcons.Icons.GodotLogoDisconnected
            e.presentation.text = "Not Connected to Godot Editor"

            if (host.model.editorState.valueOrDefault(GodotEditorState.Disconnected) == GodotEditorState.Connected) {
                e.presentation.icon = GodotIcons.Icons.GodotLogo
                e.presentation.text = "Connected to Godot Editor"
            }
        }
    }
}