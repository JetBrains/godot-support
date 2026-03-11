package com.jetbrains.rider.godot.community

import com.intellij.openapi.project.Project
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.StateFlow
import java.nio.file.Path

interface GodotProjectProvider {
    fun getGodotExecutablePathFlow(project: Project): StateFlow<Path?>
    fun getGodotProjectBasePathFlow(project: Project): StateFlow<Path?>
    fun isPureGdScriptProjectFlow(project: Project): StateFlow<Boolean?>

    fun isGodotProject(project: Project): Deferred<Boolean>
    fun getGodotMajorVersion(project: Project): GodotMajorVersion = GodotMajorVersion.GODOT_4

    fun getEditorLaunchArguments(project: Project): List<String>? = null
    fun getEditorEnvironmentVariables(project: Project): Map<String, String>? = null
    fun getIsPassParentEnvironmentVariables(project: Project): Boolean = true
    fun getWorkingDirectory(project: Project): Path? = getGodotProjectBasePathFlow(project).value
}
