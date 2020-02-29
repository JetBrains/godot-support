package com.jetbrains.rider.plugins.godot.run

import com.intellij.execution.ExecutionTargetManager
import com.intellij.execution.RunManager
import com.intellij.execution.RunManagerEx
import com.intellij.execution.configurations.ConfigurationTypeUtil
import com.intellij.execution.impl.RunConfigurationBeforeRunProvider
import com.intellij.openapi.project.Project
import com.jetbrains.rdclient.util.idea.LifetimedProjectComponent
import com.jetbrains.rider.plugins.godot.GodotProjectDiscoverer
import com.jetbrains.rider.plugins.godot.GodotServer
import com.jetbrains.rider.run.configurations.dotNetExe.DotNetExeConfiguration
import com.jetbrains.rider.run.configurations.dotNetExe.DotNetExeConfigurationType
import com.jetbrains.rider.run.configurations.exe.ExeConfiguration
import com.jetbrains.rider.run.configurations.exe.ExeConfigurationType
import com.jetbrains.rider.run.configurations.remote.DotNetRemoteConfiguration
import com.jetbrains.rider.run.configurations.remote.MonoRemoteConfigType

class GodotRunConfigurationGenerator(project: Project) : LifetimedProjectComponent(project) {

    companion object {
        const val ATTACH_CONFIGURATION_NAME = "Attach to Player"
        const val DEBUG_CONFIGURATION_NAME = "Debug Player"
        const val RUN_CONFIGURATION_NAME = "Run Player"
        const val PROFILE_CONFIGURATION_NAME = "Profile Player"
    }

    init {
        val projectDiscoverer = GodotProjectDiscoverer.getInstance(project)
        if (projectDiscoverer.getIsGodotProject) {
            val runManager = RunManager.getInstance(project)
            // Add configuration, if it doesn't exist
            if (!runManager.allSettings.any { it.type is MonoRemoteConfigType && it.name == ATTACH_CONFIGURATION_NAME }) {
                val configurationType = ConfigurationTypeUtil.findConfigurationType(MonoRemoteConfigType::class.java)
                val runConfiguration = runManager.createConfiguration(ATTACH_CONFIGURATION_NAME, configurationType.factory)
                val remoteConfig = runConfiguration.configuration as DotNetRemoteConfiguration
                remoteConfig.port = projectDiscoverer.port
                // Not shared, as that requires the entire team to have same port
                runManager.addConfiguration(runConfiguration)
            }

            if (!runManager.allSettings.any { it.type is ExeConfigurationType && it.name == RUN_CONFIGURATION_NAME }) {
                val configurationType = ConfigurationTypeUtil.findConfigurationType(ExeConfigurationType::class.java)
                val runConfiguration = runManager.createConfiguration(RUN_CONFIGURATION_NAME, configurationType.factory)
                val config = runConfiguration.configuration as ExeConfiguration
                config.parameters.exePath = "C:/Windows/System32/cmd.exe"
                config.parameters.programParameters = "/c start \"\" \"${GodotServer.getPath(project)}\" \"--path\" \"${project.basePath}\""
                config.parameters.workingDirectory = "${project.basePath}"
                config.hideDisabledExecutorButtons()
                runManager.addConfiguration(runConfiguration)
            }

            if (!runManager.allSettings.any { it.type is MonoRemoteConfigType && it.name == DEBUG_CONFIGURATION_NAME }) {
                val configurationType = ConfigurationTypeUtil.findConfigurationType(MonoRemoteConfigType::class.java)
                val runnerAndConfigurationSettings = runManager.createConfiguration(DEBUG_CONFIGURATION_NAME, configurationType.factory)
                val remoteConfig = runnerAndConfigurationSettings.configuration as DotNetRemoteConfiguration
                remoteConfig.port = projectDiscoverer.port

                // Not shared, as that requires the entire team to have same port
                runManager.addConfiguration(runnerAndConfigurationSettings)

                // add before run task
                val provider = RunConfigurationBeforeRunProvider.getProvider(project, RunConfigurationBeforeRunProvider.ID)
                val exeRunConfigurationType = ConfigurationTypeUtil.findConfigurationType(ExeConfigurationType::class.java)
                val exeRunConfigurationSettings = runManager.findConfigurationByTypeAndName(exeRunConfigurationType, RUN_CONFIGURATION_NAME)!!
                val exeRunConfiguration = exeRunConfigurationSettings.configuration
                val task = provider?.createTask(exeRunConfiguration)
                task!!.isEnabled = true
                val target = ExecutionTargetManager.getInstance(project).activeTarget
                task.setSettingsWithTarget(exeRunConfigurationSettings, target)
                task.settings!!.isActivateToolWindowBeforeRun = false
                RunManagerEx.getInstanceEx(project).setBeforeRunTasks(remoteConfig, listOf(task))
            }

            if (!runManager.allSettings.any { it.type is DotNetExeConfigurationType && it.name == PROFILE_CONFIGURATION_NAME }) {
                val configurationType = ConfigurationTypeUtil.findConfigurationType(DotNetExeConfigurationType::class.java)
                val runConfiguration = runManager.createConfiguration(PROFILE_CONFIGURATION_NAME, configurationType.factory)
                val config = runConfiguration.configuration as DotNetExeConfiguration
                config.parameters.exePath = GodotServer.getPath(project)
                config.parameters.programParameters = "--path \"${project.basePath}\""
                config.parameters.workingDirectory = "${project.basePath}"
                config.parameters.useMonoRuntime = true
                runManager.addConfiguration(runConfiguration)
            }

            // make configuration selected if nothing is selected
            if (runManager.selectedConfiguration == null) {
                val runConfiguration = runManager.findConfigurationByName(DEBUG_CONFIGURATION_NAME)
                if (runConfiguration != null) {
                    runManager.selectedConfiguration = runConfiguration
                }
            }
        }
    }
}