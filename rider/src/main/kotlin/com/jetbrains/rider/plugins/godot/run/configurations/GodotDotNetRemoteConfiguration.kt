package com.jetbrains.rider.plugins.godot.run.configurations

import com.intellij.execution.Executor
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.executors.DefaultDebugExecutor
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.project.Project
import com.jetbrains.rider.run.configurations.remote.DotNetRemoteConfiguration

class GodotDotNetRemoteConfiguration(project: Project, factory: ConfigurationFactory, name:String): DotNetRemoteConfiguration(project, factory, name) {
    override fun getState(executor: Executor, environment: ExecutionEnvironment): RunProfileState? {
        if (executor.id != DefaultDebugExecutor.EXECUTOR_ID)
            return null
        return GodotMonoConnectRemoteProfileState(this, environment)
    }
}