package com.jetbrains.rider.plugins.godot.run.configurations.gdscript

import com.intellij.execution.configurations.RunProfile
import com.intellij.openapi.project.Project
import com.intellij.platform.dap.DapLaunchArgumentsProvider
import com.intellij.platform.dap.LaunchRequestArguments

class GdScriptDapLaunchArgumentsProvider : DapLaunchArgumentsProvider {
    override fun isApplicable(profile: RunProfile): Boolean = profile is GdScriptRunConfiguration

    override fun getLaunchArguments(project: Project, profile: RunProfile): LaunchRequestArguments {
        profile as GdScriptRunConfiguration
        val structured = profile.structured
        val args = GdScriptRunConfigurationHelper.parseArgumentsToMap(profile.json)
        return LaunchRequestArguments(
            adapterId = GdScriptDebugAdapter,
            request = structured.request,
            arguments = args,
        )
    }
}
