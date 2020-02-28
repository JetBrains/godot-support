package com.jetbrains.rider.plugins.godot.run

import com.intellij.execution.RunManager
import com.intellij.execution.configurations.ConfigurationTypeUtil
import com.intellij.openapi.project.Project
import com.jetbrains.rdclient.util.idea.LifetimedProjectComponent
import com.jetbrains.rider.plugins.godot.GodotProjectDiscoverer
import com.jetbrains.rider.run.configurations.remote.DotNetRemoteConfiguration
import com.jetbrains.rider.run.configurations.remote.MonoRemoteConfigType

class GodotRunConfigurationGenerator(project: Project) : LifetimedProjectComponent(project) {

    companion object {
        const val ATTACH_CONFIGURATION_NAME = "Attach to Godot Player"
    }

    init {
        val projectDiscoverer = GodotProjectDiscoverer.getInstance(project)
        val runManager = RunManager.getInstance(project)
        if (projectDiscoverer.getIsGodotProject) {
            // Add configuration, if it doesn't exist
            if (!runManager.allSettings.any { it.type is MonoRemoteConfigType}) {
                val configurationType = ConfigurationTypeUtil.findConfigurationType(MonoRemoteConfigType::class.java)
                val runConfiguration = runManager.createConfiguration(ATTACH_CONFIGURATION_NAME, configurationType.factory)
                val remoteConfig = runConfiguration.configuration as DotNetRemoteConfiguration
                remoteConfig.port = projectDiscoverer.port
                // Not shared, as that requires the entire team to have same port
                runManager.addConfiguration(runConfiguration)
            }

            // make configuration selected if nothing is selected
            if (runManager.selectedConfiguration == null) {
                val runConfiguration = runManager.findConfigurationByName(ATTACH_CONFIGURATION_NAME)
                if (runConfiguration != null) {
                    runManager.selectedConfiguration = runConfiguration
                }
            }
        }
    }
}