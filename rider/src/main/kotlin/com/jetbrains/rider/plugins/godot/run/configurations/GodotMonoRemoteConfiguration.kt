package com.jetbrains.rider.plugins.godot.run.configurations

import com.intellij.execution.runners.ExecutionEnvironment
import com.jetbrains.rider.run.ConsoleKind
import com.jetbrains.rider.run.configurations.remote.MonoConnectRemoteProfileState
import com.jetbrains.rider.run.configurations.remote.RemoteConfiguration

open class GodotMonoConnectRemoteProfileState(remoteConfiguration: RemoteConfiguration, executionEnvironment: ExecutionEnvironment): MonoConnectRemoteProfileState(remoteConfiguration, executionEnvironment) {
    override val consoleKind: ConsoleKind = ConsoleKind.Normal
}