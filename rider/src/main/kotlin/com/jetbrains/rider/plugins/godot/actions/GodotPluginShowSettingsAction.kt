package com.jetbrains.rider.plugins.godot.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.DumbAwareAction
import com.jetbrains.rider.plugins.godot.GodotPluginBundle

class GodotPluginShowSettingsAction : DumbAwareAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        ShowSettingsUtil.getInstance().showSettingsDialog(project, GodotPluginBundle.message("configurable.name.godot.engine"))
    }
}
