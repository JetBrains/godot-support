package com.jetbrains.rider.plugins.godot.rd

import com.intellij.openapi.project.Project
import com.jetbrains.rider.godot.community.EditorConnectionState
import com.jetbrains.rider.godot.community.GodotEditorConnectionProvider

class GodotRdEditorConnectionProvider : GodotEditorConnectionProvider {
    override fun getEditorConnectionState(project: Project): EditorConnectionState {
        return GodotRdClientService.getInstance(project).currentState
    }
}
