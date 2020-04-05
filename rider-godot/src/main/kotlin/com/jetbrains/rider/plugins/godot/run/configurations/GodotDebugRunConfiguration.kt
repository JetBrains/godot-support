package com.jetbrains.rider.plugins.godot.run.configurations

import com.intellij.execution.Executor
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.configurations.WithoutOwnBeforeRunSteps
import com.intellij.execution.executors.DefaultDebugExecutor
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.runners.RunConfigurationWithSuppressedDefaultRunAction
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.annotations.Transient
import com.jetbrains.rider.plugins.unity.run.configurations.GodotDebugProfileState
import com.jetbrains.rider.run.configurations.remote.DotNetRemoteConfiguration
import com.jetbrains.rider.run.configurations.remote.RemoteConfiguration
import org.jdom.Element

class GodotDebugRunConfiguration(project: Project, factory: ConfigurationFactory )
    : DotNetRemoteConfiguration(project, factory, "Debug (Start and Attach)"),
        RunConfigurationWithSuppressedDefaultRunAction,
        RemoteConfiguration,
        WithoutOwnBeforeRunSteps {

    @Transient override var port: Int = -1
    @Transient override var address: String = "127.0.0.1"


    override fun hideDisabledExecutorButtons(): Boolean {
        return true
    }

    override var listenPortForConnections: Boolean = true

    override fun getState(executor: Executor, environment: ExecutionEnvironment): RunProfileState? {
        if (executor.id == DefaultDebugExecutor.EXECUTOR_ID)
            return GodotDebugProfileState(this, environment)
        return super.getState(executor, environment)
    }

    override fun readExternal(element: Element) {
        super.readExternal(element)
        // Reset pid, address + port to defaults. It makes no sense to persist the pid across sessions.
        port = -1
        address = "127.0.0.1"
    }
}