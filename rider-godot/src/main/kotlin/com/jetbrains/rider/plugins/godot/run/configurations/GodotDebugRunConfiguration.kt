package com.jetbrains.rider.plugins.godot.run.configurations

import com.intellij.execution.Executor
import com.intellij.execution.RunManager
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationTypeUtil
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.executors.DefaultDebugExecutor
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.runners.RunConfigurationWithSuppressedDefaultRunAction
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.annotations.Transient
import com.jetbrains.rider.plugins.godot.GodotServer
import com.jetbrains.rider.plugins.godot.run.GodotRunConfigurationGenerator
import com.jetbrains.rider.run.configurations.dotNetExe.DotNetExeConfiguration
import com.jetbrains.rider.run.configurations.dotNetExe.DotNetExeConfigurationType
import com.jetbrains.rider.run.configurations.exe.ExeConfigurationParameters
import com.jetbrains.rider.run.configurations.remote.DotNetRemoteConfiguration
import com.jetbrains.rider.run.configurations.remote.RemoteConfiguration
import org.jdom.Element
import java.io.File

class GodotDebugRunConfiguration(project: Project, factory: ConfigurationFactory )
    : DotNetRemoteConfiguration(project, factory, "Debug (Start and Attach)"),
        RunConfigurationWithSuppressedDefaultRunAction,
        RemoteConfiguration {

    @Transient override var port: Int = -1
    @Transient override var address: String = "127.0.0.1"
    var exeConfigurationParameters: ExeConfigurationParameters =
            ExeConfigurationParameters("","", "", mapOf(), false, false)

    override fun hideDisabledExecutorButtons(): Boolean {
        return true
    }

    override var listenPortForConnections: Boolean = true

    override fun getConfigurationEditor(): SettingsEditor<out GodotDebugRunConfiguration> {
        return GodotDebugRunConfigurationEditor(project)
    }

    override fun getState(executor: Executor, environment: ExecutionEnvironment): RunProfileState? {
        if (executor.id == DefaultDebugExecutor.EXECUTOR_ID)
            return GodotDebugProfileState(this, environment)

        val runManager = RunManager.getInstance(project)
        val godotPath = File(GodotServer.getGodotPath(project))
        val configurationType = ConfigurationTypeUtil.findConfigurationType(DotNetExeConfigurationType::class.java)
        val runConfiguration = runManager.createConfiguration(GodotRunConfigurationGenerator.PROFILE_CONFIGURATION_NAME, configurationType.factory)
        val config = runConfiguration.configuration as DotNetExeConfiguration
        config.parameters.exePath = godotPath.absolutePath
        config.parameters.programParameters = "--path \"${project.basePath}\""
        config.parameters.workingDirectory = "${project.basePath}"
        config.parameters.useMonoRuntime = true
        return config.getState(executor, environment)
    }

    override fun readExternal(element: Element) {
        super.readExternal(element)
        // Reset pid, address + port to defaults. It makes no sense to persist the pid across sessions.
        port = -1
        address = "127.0.0.1"
    }
}