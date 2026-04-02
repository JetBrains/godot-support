package com.jetbrains.rider.godot.community.actions

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.project.DumbAware
import com.intellij.rider.plugins.godot.community.icons.RiderPluginsGodotCommunityIcons
import com.jetbrains.rider.godot.community.EditorConnectionState
import com.jetbrains.rider.godot.community.GodotCommunityBundle
import com.jetbrains.rider.godot.community.utils.GodotCommunityUtil

class GodotActionsToolbar : DefaultActionGroup(), DumbAware {
    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT
    override fun update(e: AnActionEvent) {
        val project = e.project
        if (project == null) {
            e.presentation.isVisible = false
            return
        }

        val isGodotProject = GodotCommunityUtil.isGodotProject(project)
        e.presentation.isVisible = isGodotProject


        // Determine icon/text based on connection state
        when (GodotCommunityUtil.getEditorConnectionState(project)) {
            EditorConnectionState.CONNECTED -> {
                e.presentation.icon = RiderPluginsGodotCommunityIcons.Godot
                e.presentation.text = GodotCommunityBundle.message("connected.to.godot.editor.text")
            }

            EditorConnectionState.DISCONNECTED -> {
                e.presentation.icon = RiderPluginsGodotCommunityIcons.GodotDisconnected
                e.presentation.text = GodotCommunityBundle.message("not.connected.to.godot.editor.text")
            }

            EditorConnectionState.NOT_APPLICABLE -> {
                // No connection concept (pure GDScript) - show default connected icon
                e.presentation.icon = RiderPluginsGodotCommunityIcons.Godot
                e.presentation.text = GodotCommunityBundle.message("godot.toolbar.text")
            }
        }
    }
}


