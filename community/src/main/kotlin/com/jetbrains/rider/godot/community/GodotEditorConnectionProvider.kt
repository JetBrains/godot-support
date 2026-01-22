package com.jetbrains.rider.godot.community

import com.intellij.openapi.project.Project

interface GodotEditorConnectionProvider {
    fun getEditorConnectionState(project: Project): EditorConnectionState
}

enum class EditorConnectionState {
    CONNECTED,
    DISCONNECTED,
    NOT_APPLICABLE
}
