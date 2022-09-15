package com.jetbrains.rider.plugins.godot.run

import com.intellij.execution.RunManager
import com.intellij.execution.configurations.ConfigurationTypeUtil
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.jetbrains.rd.util.reactive.adviseNotNull
import com.jetbrains.rd.util.reactive.whenTrue
import com.jetbrains.rdclient.util.idea.LifetimedProjectComponent
import com.jetbrains.rider.plugins.godot.GodotProjectDiscoverer
import com.jetbrains.rider.plugins.godot.run.configurations.GodotDebugRunConfiguration
import com.jetbrains.rider.plugins.godot.run.configurations.GodotDebugRunConfigurationType
import com.jetbrains.rider.projectView.solution
import com.jetbrains.rider.run.configurations.dotNetExe.DotNetExeConfiguration
import com.jetbrains.rider.run.configurations.dotNetExe.DotNetExeConfigurationType
import com.jetbrains.rider.run.configurations.remote.DotNetRemoteConfiguration
import com.jetbrains.rider.run.configurations.remote.MonoRemoteConfigType
import com.jetbrains.rider.runtime.dotNetCore.DotNetCoreRuntimeType

class GodotRunConfigurationGenerator(project: Project) : LifetimedProjectComponent(project) {

    companion object {
        const val ATTACH_CONFIGURATION_NAME = "Attach to Player"

        const val PLAYER_CONFIGURATION_NAME = "Player"
        const val EDITOR_CONFIGURATION_NAME = "Editor"
        const val WAT_UNIT_TESTING = "WATSharpGui"

        private val logger = Logger.getInstance(GodotRunConfigurationGenerator::class.java)
    }

    init {
        project.solution.isLoaded.whenTrue(componentLifetime){
            val godotDiscoverer = GodotProjectDiscoverer.getInstance(project)
            godotDiscoverer.isGodotProject.whenTrue(componentLifetime) { lt->
                logger.info("isGodotProject = true")
                val runManager = RunManager.getInstance(project)

                GodotProjectDiscoverer.getInstance(project).godotCorePath.advise(lt) { corePath->
                    if (corePath != null) {
                        val toRemove = runManager.allSettings.filter {
                            it.type is MonoRemoteConfigType && it.name == ATTACH_CONFIGURATION_NAME
                        }
                        for (value in toRemove) {
                            runManager.removeConfiguration(value)
                        }
                    }
                    else{
                        if (!runManager.allSettings.any { it.type is MonoRemoteConfigType && it.name == ATTACH_CONFIGURATION_NAME }) {
                            val configurationType = ConfigurationTypeUtil.findConfigurationType(MonoRemoteConfigType::class.java)
                            val runConfiguration = runManager.createConfiguration(ATTACH_CONFIGURATION_NAME, configurationType.factory)
                            val remoteConfig = runConfiguration.configuration as DotNetRemoteConfiguration
                            remoteConfig.port = godotDiscoverer.port
                            runConfiguration.storeInLocalWorkspace()
                            runManager.addConfiguration(runConfiguration)
                        }
                    }
                }

                GodotProjectDiscoverer.getInstance(project).godotMonoPath.adviseNotNull(lt){ path ->
                    createOrUpdateRunConfiguration(PLAYER_CONFIGURATION_NAME, "--path \"${project.basePath}\"", runManager, path, project)
                    createOrUpdateRunConfiguration(EDITOR_CONFIGURATION_NAME, "--path \"${project.basePath}\" --editor", runManager, path, project)
                    if (godotDiscoverer.hasWATAddon.value)
                        createOrUpdateRunConfiguration(WAT_UNIT_TESTING, "--path \"${project.basePath}\" \"res://addons/WAT/gui.tscn\"", runManager, path, project)
                }

                GodotProjectDiscoverer.getInstance(project).godotCorePath.adviseNotNull(lt){ path ->
                    createOrUpdateCoreRunConfiguration(PLAYER_CONFIGURATION_NAME, "--path \"${project.basePath}\"", runManager, path, project)
                    createOrUpdateCoreRunConfiguration(EDITOR_CONFIGURATION_NAME, "--path \"${project.basePath}\" --editor", runManager, path, project)
                    if (godotDiscoverer.hasWATAddon.value)
                        createOrUpdateCoreRunConfiguration(WAT_UNIT_TESTING, "--path \"${project.basePath}\" \"res://addons/WAT/gui.tscn\"", runManager, path, project)
                }

                // make configuration selected if nothing is selected
                if (runManager.selectedConfiguration == null) {
                    val runConfiguration = runManager.findConfigurationByName(PLAYER_CONFIGURATION_NAME)
                    if (runConfiguration != null) {
                        runManager.selectedConfiguration = runConfiguration
                    }
                }
            }
        }
    }

    private fun createOrUpdateCoreRunConfiguration(
        configurationName: String,
        programParameters:String,
        runManager: RunManager,
        godotPath: String,
        project: Project
    ) {
        val configs = runManager.allSettings.filter { it.type is DotNetExeConfiguration && it.name == configurationName }
        if (configs.any()) {
            configs.forEach{
                (it.configuration as DotNetExeConfiguration).parameters.exePath = godotPath
            }
        } else {
            val configurationType = ConfigurationTypeUtil.findConfigurationType(DotNetExeConfigurationType::class.java)
            val runConfiguration = runManager.createConfiguration(configurationName, configurationType.factory)
            val config = runConfiguration.configuration as DotNetExeConfiguration
            config.parameters.exePath = godotPath
            config.parameters.programParameters = programParameters
            config.parameters.workingDirectory = "${project.basePath}"
            config.parameters.runtimeType = DotNetCoreRuntimeType
            runConfiguration.storeInLocalWorkspace()
            runManager.addConfiguration(runConfiguration)
        }
    }

    private fun createOrUpdateRunConfiguration(
        configurationName: String,
        programParameters:String,
        runManager: RunManager,
        godotPath: String,
        project: Project
    ) {
        val configs = runManager.allSettings.filter { it.type is GodotDebugRunConfigurationType && it.name == configurationName }
        if (configs.any()) {
            configs.forEach{
                (it.configuration as GodotDebugRunConfiguration).parameters.exePath = godotPath
            }
        } else {
            val configurationType = ConfigurationTypeUtil.findConfigurationType(GodotDebugRunConfigurationType::class.java)
            val runConfiguration = runManager.createConfiguration(configurationName, configurationType.factory)
            val config = runConfiguration.configuration as GodotDebugRunConfiguration
            config.parameters.exePath = godotPath
            config.parameters.programParameters = programParameters
            config.parameters.workingDirectory = "${project.basePath}"
            runConfiguration.storeInLocalWorkspace()
            runManager.addConfiguration(runConfiguration)
        }
    }
}