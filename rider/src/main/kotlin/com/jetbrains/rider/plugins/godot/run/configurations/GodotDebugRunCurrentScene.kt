package com.jetbrains.rider.plugins.godot.run.configurations

import com.intellij.execution.ExecutionException
import com.intellij.execution.Executor
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import com.intellij.execution.configurations.ConfigurationTypeBase
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.configurations.VirtualConfigurationType
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.application.EDT
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.rd.util.lifetime
import com.intellij.util.execution.ParametersListUtil
import com.jetbrains.rider.godot.community.GdProjectGodotService
import com.jetbrains.rider.plugins.godot.GodotPluginBundle
import com.jetbrains.rider.run.configurations.TerminalMode
import com.jetbrains.rider.run.configurations.dotNetExe.DotNetExeConfiguration
import com.jetbrains.rider.run.configurations.dotNetExe.DotNetExeConfigurationParameters
import com.jetbrains.rider.run.configurations.dotNetExe.DotNetExeExecutorFactory
import com.jetbrains.rider.runtime.dotNetCore.DotNetCoreRuntimeType
import icons.RiderIcons
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.annotations.NonNls

private fun buildCurrentSceneProgramParameters(workingDirectory: String, scene: String): String =
    ParametersListUtil.join(listOf("--path", workingDirectory, scene))

class GodotDebugRunCurrentSceneFactory(type: ConfigurationType) : ConfigurationFactory(type) {
    override fun getId(): String = "Godot Current Scene"
    override fun createTemplateConfiguration(project: Project): RunConfiguration =
        GodotDebugRunCurrentScene(project, this)
}

class GodotDebugRunCurrentSceneType : ConfigurationTypeBase(
    ID,
    GodotPluginBundle.message("godot.run.current.scene"),
    GodotPluginBundle.message("godot.run.current.scene.description"),
    RiderIcons.RunConfigurations.DotNetExecutable
), VirtualConfigurationType, DumbAware {
    val factory: GodotDebugRunCurrentSceneFactory = GodotDebugRunCurrentSceneFactory(this)

    init {
        addFactory(factory)
    }

    companion object {
        @NonNls
        const val ID: String = "GodotCurrentScene"
    }
}

class GodotDebugRunCurrentScene private constructor(
    project: Project,
    factory: ConfigurationFactory,
    parameters: DotNetExeConfigurationParameters
) : DotNetExeConfiguration(CONFIGURATION_NAME, project, factory, parameters) {

    constructor(project: Project, factory: ConfigurationFactory) : this(project, factory, defaultParameters(project))

    companion object {
        @NonNls
        const val CONFIGURATION_NAME: String = "Current Scene"

        /**
         * The executable and the working directory are filled in by
         * [com.jetbrains.rider.plugins.godot.run.GodotRunConfigurationGenerator] once the Godot installation
         * and the project location are known.
         */
        private fun defaultParameters(project: Project) = DotNetExeConfigurationParameters(
            project = project,
            exePath = "",
            programParameters = "",
            workingDirectory = "",
            envs = hashMapOf(),
            isPassParentEnvs = true,
            executeAsIs = false,
            assemblyToDebug = null,
            runtimeArguments = "",
            runtimeType = DotNetCoreRuntimeType,
            envFilePaths = emptyList(),
            redirectInputPath = null,
            terminalMode = TerminalMode.Auto,
        )
    }

    override fun clone(): RunConfiguration {
        val newConfiguration = GodotDebugRunCurrentScene(project, factory!!, parameters.copy())
        newConfiguration.doCopyOptionsFrom(this)
        copyCopyableDataTo(newConfiguration)
        return newConfiguration
    }

    override suspend fun getRunProfileStateAsync(executor: Executor, environment: ExecutionEnvironment): RunProfileState {
        val scene = GdProjectGodotService.getInstance(project).currentSceneFlow.value
        if (scene == null) throw ExecutionException(GodotPluginBundle.message("godot.run.current.scene.no.scene.info"))
        if (scene.isEmpty()) throw ExecutionException(GodotPluginBundle.message("godot.run.current.scene.no.scene.selected"))

        val launchParameters = parameters.copy().apply {
            programParameters = buildCurrentSceneProgramParameters(workingDirectory, scene)
        }
        return withContext(Dispatchers.EDT) {
            DotNetExeExecutorFactory(launchParameters).create(executor.id, environment, environment.project.lifetime)
        }
    }
}
