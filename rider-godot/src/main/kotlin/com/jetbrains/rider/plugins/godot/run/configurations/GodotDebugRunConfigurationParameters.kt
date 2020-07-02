package com.jetbrains.rider.plugins.godot.run.configurations

import com.jetbrains.rider.run.configurations.exe.ExeConfigurationParameters

class GodotDebugRunConfigurationParameters(
    exePath: String,
    programParameters: String,
    workingDirectory: String,
    envs: Map<String, String>,
    isPassParentEnvs: Boolean,
    useExternalConsole: Boolean
) : ExeConfigurationParameters(
    exePath,
    programParameters,
    workingDirectory,
    envs,
    isPassParentEnvs,
    useExternalConsole
)
