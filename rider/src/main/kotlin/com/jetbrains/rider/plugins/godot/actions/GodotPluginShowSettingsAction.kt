package com.jetbrains.rider.plugins.godot.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.DumbAwareAction

class GodotPluginShowSettingsAction : DumbAwareAction() {
    companion object {
        const val actionId = "ShowGodotSettingsInRider"
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        ShowSettingsUtil.getInstance().showSettingsDialog(project, "Godot Engine")
    }
}
