package com.jetbrains.rider.plugins.godot.run.configurations

import com.intellij.execution.configurations.RunConfiguration
import com.intellij.openapi.project.Project
import com.jetbrains.rider.run.configurations.DotNetConfigurationFactoryBase
import com.jetbrains.rider.run.configurations.remote.DotNetRemoteConfiguration
import com.jetbrains.rider.run.configurations.remote.MonoRemoteConfigType

class GodotDotNetRemoteConfigurationFactory(monoRemoteConfigType: MonoRemoteConfigType) : DotNetConfigurationFactoryBase<DotNetRemoteConfiguration>(monoRemoteConfigType){
    override fun getId(): String {
        // super.getId() does the same, but prints a deprecation message
        return name
    }

    override fun createTemplateConfiguration(project: Project): RunConfiguration = GodotDotNetRemoteConfiguration(project, this, "")
}