package com.jetbrains.rider.plugins.godot.rd

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.project.BaseProjectDirectories
import com.intellij.openapi.project.DumbAwareAction
import com.jetbrains.rider.godot.community.utils.GodotCommunityUtil

class OpenInGodotAction : DumbAwareAction() {
    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT
    override fun update(e: AnActionEvent) {
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE)
        val project = e.project
        if (file == null || project == null || !BaseProjectDirectories.getInstance(project).contains(file)) {
            e.presentation.isEnabledAndVisible = false
            return
        }
        e.presentation.isVisible = GodotCommunityUtil.isGodotProject(project)
        // TODO: This guards against files outside of the project
        // such as scratch files. Hidden files are a bit more complicated,
        // since there is an godot option to show them.
        e.presentation.isEnabled = GodotRdClientService.getInstanceIfCreated(project)?.isConnected ?: false
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE) ?: return
        GodotRdClientService.getInstanceIfCreated(project)?.openInGodot(file)
    }
}
