package com.jetbrains.rider.plugins.godot

import com.intellij.openapi.project.Project
import com.jetbrains.rd.util.reactive.valueOrDefault
import com.jetbrains.rider.godot.community.EditorConnectionState
import com.jetbrains.rider.godot.community.GodotEditorConnectionProvider
import com.jetbrains.rider.model.godot.frontendBackend.GodotEditorState
import com.jetbrains.rider.model.godot.frontendBackend.godotFrontendBackendModel
import com.jetbrains.rider.projectView.solution

class RiderEditorConnectionProvider : GodotEditorConnectionProvider {
    override fun getEditorConnectionState(project: Project): EditorConnectionState {
        val discoverer = GodotProjectDiscoverer.getInstance(project)
        val descriptor = discoverer.godotDescriptor.valueOrNull ?: return EditorConnectionState.NOT_APPLICABLE

        // Pure GDScript projects don't have connection state
        if (descriptor.isPureGdScriptProject) {
            return EditorConnectionState.NOT_APPLICABLE
        }

        // C# projects - return actual connection state
        val editorState = project.solution.godotFrontendBackendModel
            .editorState.valueOrDefault(GodotEditorState.Disconnected)

        return when (editorState) {
            GodotEditorState.Connected -> EditorConnectionState.CONNECTED
            GodotEditorState.Disconnected -> EditorConnectionState.DISCONNECTED
        }
    }
}
