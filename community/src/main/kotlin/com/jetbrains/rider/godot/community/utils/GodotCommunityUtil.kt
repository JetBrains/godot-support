package com.jetbrains.rider.godot.community.utils

import com.intellij.openapi.extensions.ExtensionPointName
import com.intellij.openapi.project.Project
import com.jetbrains.rider.godot.community.EditorConnectionState
import com.jetbrains.rider.godot.community.GodotEditorConnectionProvider
import com.jetbrains.rider.godot.community.GodotMajorVersion
import com.jetbrains.rider.godot.community.actions.GodotEditorLaunchConfig
import com.jetbrains.rider.godot.community.GodotProjectProvider
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import java.nio.file.Path
import kotlin.io.path.exists

@OptIn(ExperimentalCoroutinesApi::class)
fun Deferred<Boolean>?.hasCompletedTrue(): Boolean {
    if (this == null) return false

    return if (this.isCompleted) {
        this.getCompleted()
    } else {
        false
    }
}

private val GODOT_PROJECT_PROVIDER_EP: ExtensionPointName<GodotProjectProvider> =
    ExtensionPointName.create("com.intellij.rider.godot.community.godotProjectProvider")

private val EDITOR_CONNECTION_PROVIDER_EP: ExtensionPointName<GodotEditorConnectionProvider> =
    ExtensionPointName.create("com.intellij.rider.godot.community.editorConnectionProvider")

object GodotCommunityUtil {
    fun getGodotExecutablePath(project: Project): Path? =
        GODOT_PROJECT_PROVIDER_EP.extensionList.firstNotNullOfOrNull {
            val path = it.getGodotExecutablePathFlow(project).value
            if (path != null && path.exists()) path else null
        }


    fun getGodotProjectBasePath(project: Project): Path? =
        GODOT_PROJECT_PROVIDER_EP.extensionList.firstNotNullOfOrNull {
            val path = it.getGodotProjectBasePathFlow(project).value
            if (path != null && path.exists()) path else null
        }

    fun getGodotMajorVersion(project: Project): GodotMajorVersion? =
        GODOT_PROJECT_PROVIDER_EP.extensionList.firstNotNullOfOrNull {
            it.getGodotMajorVersion(project)
        }

    fun isPureGdScriptProject(project: Project): Boolean? =
        GODOT_PROJECT_PROVIDER_EP.extensionList.firstNotNullOfOrNull {
            it.isPureGdScriptProjectFlow(project).value
        }

    fun isPureGdScriptProjectFlow(project: Project): StateFlow<Boolean?> =
        GODOT_PROJECT_PROVIDER_EP.extensionList.firstNotNullOfOrNull {
            it.isPureGdScriptProjectFlow(project)
        } ?: MutableStateFlow(null)

    fun getGodotExecutablePathFlow(project: Project): Flow<Path?> {
        val flows = GODOT_PROJECT_PROVIDER_EP.extensionList
            .map { it.getGodotExecutablePathFlow(project) }

        if (flows.isEmpty()) return flowOf(null)

        return combine(flows) { paths ->
            paths.firstOrNull { it != null && it.exists() }
        }
    }

    fun getGodotProjectBasePathFlow(project: Project): Flow<Path?> {
        val flows = GODOT_PROJECT_PROVIDER_EP.extensionList
            .map { it.getGodotProjectBasePathFlow(project) }

        if (flows.isEmpty()) return flowOf(null)

        return combine(flows) { paths ->
            paths.firstOrNull { it != null && it.exists() }
        }
    }

    fun getEditorConnectionState(project: Project): EditorConnectionState =
        EDITOR_CONNECTION_PROVIDER_EP.extensionList
            .firstNotNullOfOrNull {
                it.getEditorConnectionState(project).takeIf { state -> state != EditorConnectionState.NOT_APPLICABLE }
            } ?: EditorConnectionState.NOT_APPLICABLE

    fun isGodotProject(project: Project): Deferred<Boolean>? =
        GODOT_PROJECT_PROVIDER_EP.extensionList.firstNotNullOfOrNull { it.isGodotProject(project) }

    fun getEditorLaunchConfig(project: Project): GodotEditorLaunchConfig? {
        val executablePath = getGodotExecutablePath(project) ?: return null
        val projectPath = getGodotProjectBasePath(project) ?: return null

        // Check for custom arguments/environment from providers
        val customArgs = GODOT_PROJECT_PROVIDER_EP.extensionList
            .firstNotNullOfOrNull { it.getEditorLaunchArguments(project) }
        val customEnv = GODOT_PROJECT_PROVIDER_EP.extensionList
            .firstNotNullOfOrNull { it.getEditorEnvironmentVariables(project) }
        val customWorkDir = GODOT_PROJECT_PROVIDER_EP.extensionList
            .firstNotNullOfOrNull { it.getWorkingDirectory(project) }
        val isPassParentEnvs = GODOT_PROJECT_PROVIDER_EP.extensionList
            .map { it.getIsPassParentEnvironmentVariables(project) }
            .firstOrNull()

        return GodotEditorLaunchConfig(
            executablePath = executablePath,
            workingDirectory = customWorkDir ?: projectPath,
            isPassParentEnvs = isPassParentEnvs ?: true,
            arguments = customArgs ?: listOf("--editor", "--path", projectPath.toString()),
            environmentVariables = customEnv ?: emptyMap()
        )
    }
}

