package com.jetbrains.rider.plugins.godot.run.configurations

import com.intellij.execution.CantRunException
import com.intellij.execution.ExecutionException
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import com.intellij.execution.configurations.ConfigurationTypeBase
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.execution.configurations.RunConfigurationSingletonPolicy
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.configurations.RuntimeConfigurationError
import com.intellij.execution.executors.DefaultDebugExecutor
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.runners.RunConfigurationWithSuppressedDefaultRunAction
import com.intellij.openapi.application.EDT
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.util.NlsSafe
import com.intellij.ui.dsl.builder.panel
import com.intellij.util.execution.ParametersListUtil
import com.jetbrains.rd.util.lifetime.Lifetime
import com.jetbrains.rider.build.tasks.SolutionRunConfiguration
import com.jetbrains.rider.debugger.IRiderDebuggable
import com.jetbrains.rider.godot.community.GdProjectGodotService
import com.jetbrains.rider.plugins.godot.GodotPluginBundle
import com.jetbrains.rider.plugins.godot.GodotProjectDiscoverer
import com.jetbrains.rider.plugins.godot.rd.GodotRdClientService
import com.jetbrains.rider.run.configurations.AsyncExecutorFactory
import com.jetbrains.rider.run.configurations.RiderAsyncRunConfiguration
import com.jetbrains.rider.run.configurations.RiderConfigurationParametersAware
import com.jetbrains.rider.run.configurations.TerminalMode
import com.jetbrains.rider.run.configurations.dotNetExe.DotNetExeConfigurationParameters
import com.jetbrains.rider.run.configurations.dotNetExe.DotNetExeExecutorFactory
import com.jetbrains.rider.runtime.RiderDotNetActiveRuntimeHost
import com.jetbrains.rider.runtime.dotNetCore.DotNetCoreRuntimeType
import icons.RiderIcons
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jdom.Element
import org.jetbrains.annotations.NonNls
import javax.swing.JComponent

private fun buildCurrentSceneProgramParameters(workingDirectory: String, scene: String): String =
    ParametersListUtil.join(listOf("--path", workingDirectory, scene))

enum class GodotCurrentSceneLaunchSource(private val messageKey: String) {
    FROM_GODOT_EDITOR("godot.run.current.scene.source.godot.editor"),
    FROM_RIDER("godot.run.current.scene.source.rider");

    override fun toString(): String = GodotPluginBundle.message(messageKey)
}

class GodotDebugRunCurrentSceneFactory(type: ConfigurationType) : ConfigurationFactory(type) {
    override fun getId(): String = "Godot Current Scene"

    override fun getSingletonPolicy(): RunConfigurationSingletonPolicy = RunConfigurationSingletonPolicy.SINGLE_INSTANCE_ONLY

    override fun createTemplateConfiguration(project: Project): RunConfiguration = GodotDebugRunCurrentScene(project, this)
}

class GodotDebugRunCurrentSceneType : ConfigurationTypeBase(
    ID,
    GodotDebugRunCurrentScene.CONFIGURATION_NAME,
    GodotPluginBundle.message("godot.run.current.scene.description"),
    RiderIcons.RunConfigurations.DotNetExecutable,
), DumbAware {
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
    override val parameters: DotNetExeConfigurationParameters,
) : RiderAsyncRunConfiguration(
    CONFIGURATION_NAME,
    project,
    factory,
    { Editor() },
    GodotCurrentSceneExecutorFactory(project),
), IRiderDebuggable, RunConfigurationWithSuppressedDefaultRunAction, SolutionRunConfiguration,
    RiderConfigurationParametersAware<DotNetExeConfigurationParameters> {

    constructor(project: Project, factory: ConfigurationFactory) : this(project, factory, defaultParameters(project))

    var launchSource: GodotCurrentSceneLaunchSource = GodotCurrentSceneLaunchSource.FROM_GODOT_EDITOR

    override fun checkConfiguration() {
        super.checkConfiguration()
        if (!GodotRdClientService.getInstance(project).isConnected) {
            throw RuntimeConfigurationError(GodotPluginBundle.message("godot.debug.in.editor.not.connected"))
        }

        if (launchSource == GodotCurrentSceneLaunchSource.FROM_RIDER) {
            if (GodotProjectDiscoverer.getInstance(project).godot4Path.value == null) {
                throw RuntimeConfigurationError(GodotPluginBundle.message("godot.debug.in.editor.not.godot4"))
            }
        }
    }

    override fun readExternal(element: Element) {
        super.readExternal(element)
        parameters.readExternal(element)
        launchSource = GodotCurrentSceneLaunchSource.entries.firstOrNull { it.name == element.getAttributeValue(LAUNCH_SOURCE_ATTRIBUTE) }
            ?: GodotCurrentSceneLaunchSource.FROM_GODOT_EDITOR
    }

    override fun writeExternal(element: Element) {
        super.writeExternal(element)
        parameters.writeExternal(element)
        element.setAttribute(LAUNCH_SOURCE_ATTRIBUTE, launchSource.name)
    }

    override fun clone(): RunConfiguration {
        val newConfiguration = GodotDebugRunCurrentScene(project, requireNotNull(factory), parameters.copy())
        newConfiguration.launchSource = launchSource
        newConfiguration.doCopyOptionsFrom(this)
        copyCopyableDataTo(newConfiguration)
        return newConfiguration
    }

    private class Editor : SettingsEditor<GodotDebugRunCurrentScene>() {
        private val launchSourceComboBox = ComboBox(GodotCurrentSceneLaunchSource.entries.toTypedArray())

        override fun resetEditorFrom(configuration: GodotDebugRunCurrentScene) {
            launchSourceComboBox.selectedItem = configuration.launchSource
        }

        override fun applyEditorTo(configuration: GodotDebugRunCurrentScene) {
            configuration.launchSource = launchSourceComboBox.selectedItem as GodotCurrentSceneLaunchSource
        }

        override fun createEditor(): JComponent = panel {
            row {
                text(GodotPluginBundle.message("godot.run.current.scene.source.description"))
            }
            row(GodotPluginBundle.message("godot.run.current.scene.launch.source")) {
                cell(launchSourceComboBox)
            }
        }
    }

    companion object {
        @NlsSafe
        const val CONFIGURATION_NAME: String = "Player (Current Scene)"

        private const val LAUNCH_SOURCE_ATTRIBUTE = "launchSource"

        /**
         * The executable and working directory are set by [com.jetbrains.rider.plugins.godot.run.GodotRunConfigurationGenerator].
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
}

private class GodotCurrentSceneExecutorFactory(private val project: Project) : AsyncExecutorFactory {
    override suspend fun create(executorId: String, environment: ExecutionEnvironment, lifetime: Lifetime): RunProfileState {
        if (executorId != DefaultDebugExecutor.EXECUTOR_ID) {
            throw CantRunException(GodotPluginBundle.message("godot.run.current.scene.debug.only"))
        }

        if (!GodotRdClientService.getInstance(project).isConnected) {
            throw ExecutionException(GodotPluginBundle.message("godot.debug.in.editor.not.connected"))
        }

        val configuration = environment.runProfile as GodotDebugRunCurrentScene
        return when (configuration.launchSource) {
            GodotCurrentSceneLaunchSource.FROM_GODOT_EDITOR ->
                GodotInEditorDebugExecutorFactory(project).create(executorId, environment, lifetime)
            GodotCurrentSceneLaunchSource.FROM_RIDER -> {
                val scene = GdProjectGodotService.getInstance(project).currentSceneFlow.value
                    ?: throw ExecutionException(GodotPluginBundle.message("godot.run.current.scene.no.scene.info"))
                if (scene.isEmpty()) {
                    throw ExecutionException(GodotPluginBundle.message("godot.run.current.scene.no.scene.selected"))
                }

                val launchParameters = configuration.parameters.copy().apply {
                    programParameters = buildCurrentSceneProgramParameters(workingDirectory, scene)
                }
                withContext(Dispatchers.EDT) {
                    DotNetExeExecutorFactory(launchParameters).create(executorId, environment, lifetime)
                }
            }
        }
    }
}
