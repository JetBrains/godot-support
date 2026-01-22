package com.jetbrains.rider.plugins.godot.project

import com.intellij.execution.RunManager
import com.intellij.execution.configurations.ConfigurationTypeUtil
import com.intellij.openapi.project.Project
import com.intellij.util.execution.ParametersListUtil
import com.jetbrains.rider.godot.community.GodotProjectProvider
import com.jetbrains.rider.plugins.godot.GodotProjectDiscoverer
import com.jetbrains.rider.plugins.godot.run.GodotRunConfigurationGenerator
import com.jetbrains.rider.plugins.godot.run.configurations.GodotDebugRunConfigurationType
import com.jetbrains.rider.run.configurations.RiderConfigurationParametersAware
import com.jetbrains.rider.run.configurations.dotNetExe.DotNetExeConfigurationType
import com.jetbrains.rider.run.configurations.exe.ExeConfigurationParameters
import com.jetbrains.rider.run.configurations.exe.ExeConfigurationType
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.StateFlow
import java.nio.file.Path

class RiderGodotProjectProvider : GodotProjectProvider {

    override fun getGodotExecutablePath(project: Project): Path? =
        GodotProjectDiscoverer.getInstance(project).godotPath.valueOrNull?.let { Path.of(it) }

    override fun getGodotProjectBasePath(project: Project): Path? {
        return GodotProjectDiscoverer.getInstance(project).mainProjectBasePath.value
    }

    override fun getGodotExecutablePathFlow(project: Project): StateFlow<Path?> =
        GodotProjectDiscoverer.getInstance(project).executablePathFlow

    override fun getGodotProjectBasePathFlow(project: Project): StateFlow<Path?> {
        return GodotProjectDiscoverer.getInstance(project).mainProjectBasePath
    }

    override fun isGodotProject(project: Project): Deferred<Boolean> {
        return GodotProjectDiscoverer.getInstance(project).isGodotProject
    }

    override fun getEditorLaunchArguments(project: Project): List<String>? {
        val parameters = getRunConfigParameters(project) ?: return null
        return ParametersListUtil.parse(parameters.programParameters)
    }

    override fun getEditorEnvironmentVariables(project: Project): Map<String, String>? {
        val parameters = getRunConfigParameters(project) ?: return null
        return parameters.envs.takeIf { it.isNotEmpty() }
    }

    override fun getWorkingDirectory(project: Project): Path? {
        val parameters = getRunConfigParameters(project) ?: return null
        return Path.of(parameters.workingDirectory)
    }

    override fun getIsPassParentEnvironmentVariables(project: Project): Boolean {
        val parameters = getRunConfigParameters(project) ?: return true
        return parameters.isPassParentEnvs
    }

    private fun getRunConfigParameters(project: Project): ExeConfigurationParameters? {
        val runManager = RunManager.getInstance(project)
        val configurationTypes = listOf( // Correct ordering is important
            DotNetExeConfigurationType::class.java,    // Godot 4.x with .NET
            GodotDebugRunConfigurationType::class.java, // Godot 3.x
            ExeConfigurationType::class.java            // Pure GDScript projects
        )

        val runConfiguration = configurationTypes.firstNotNullOfOrNull { type ->
            runManager.findConfigurationByTypeAndName(
                ConfigurationTypeUtil.findConfigurationType(type).id,
                GodotRunConfigurationGenerator.EDITOR_CONFIGURATION_NAME
            )
        } ?: return null

        return (runConfiguration.configuration as? RiderConfigurationParametersAware<*>)?.parameters
    }
}
