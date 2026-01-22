package com.jetbrains.rider.godot.community.actions

import java.nio.file.Path

data class GodotEditorLaunchConfig(
    val executablePath: Path,
    val workingDirectory: Path,
    val arguments: List<String>,
    val isPassParentEnvs: Boolean = true,
    val environmentVariables: Map<String, String> = emptyMap()
)
