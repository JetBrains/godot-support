package com.jetbrains.rider.godot.community

import com.intellij.openapi.project.Project
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.StateFlow
import java.nio.file.Path

interface GodotProjectProvider {
    fun getGodotExecutablePath(project: Project): Path?
    fun getGodotExecutablePathFlow(project: Project): StateFlow<Path?>
    fun getGodotProjectBasePathFlow(project: Project): StateFlow<Path?>
    fun getGodotProjectBasePath(project: Project): Path?

    fun isGodotProject(project: Project): Deferred<Boolean>

    fun getEditorLaunchArguments(project: Project): List<String>? = null
    fun getEditorEnvironmentVariables(project: Project): Map<String, String>? = null
    fun getIsPassParentEnvironmentVariables(project: Project): Boolean = true
    fun getWorkingDirectory(project: Project): Path? = getGodotProjectBasePath(project)
}
