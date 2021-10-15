package com.jetbrains.rider.plugins.godot.run

import com.intellij.execution.RunManager
import com.intellij.execution.configurations.ConfigurationTypeUtil
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.jetbrains.rd.util.reactive.whenTrue
import com.jetbrains.rdclient.util.idea.LifetimedProjectComponent
import com.jetbrains.rider.plugins.godot.FrontendBackendHost
import com.jetbrains.rider.plugins.godot.GodotProjectDiscoverer
import com.jetbrains.rider.plugins.godot.run.configurations.GodotDebugRunConfiguration
import com.jetbrains.rider.plugins.godot.run.configurations.GodotDebugRunConfigurationType
import com.jetbrains.rider.projectView.solution
import com.jetbrains.rider.run.configurations.dotNetExe.DotNetExeConfigurationType
import com.jetbrains.rider.run.configurations.exe.ExeConfigurationType
import com.jetbrains.rider.run.configurations.remote.DotNetRemoteConfiguration
import com.jetbrains.rider.run.configurations.remote.MonoRemoteConfigType
import java.io.File

class GodotRunConfigurationGenerator(project: Project) : LifetimedProjectComponent(project) {

    companion object {
        const val ATTACH_CONFIGURATION_NAME = "Attach to Player"
        const val RUN_CONFIGURATION_NAME = "Run Player"
        const val PROFILE_CONFIGURATION_NAME = "Profile Player"

        const val PLAYER_CONFIGURATION_NAME = "Player"
        const val EDITOR_CONFIGURATION_NAME = "Editor"
        const val WAT_UNIT_TESTING = "WATSharpGui"

        private val logger = Logger.getInstance(GodotRunConfigurationGenerator::class.java)
    }

    init {
        project.solution.isLoaded.whenTrue(componentLifetime){
            val godotDiscoverer = GodotProjectDiscoverer.getInstance(project)
            godotDiscoverer.isGodotProject.whenTrue(componentLifetime) {
                logger.info("isGodotProject = true")
                val runManager = RunManager.getInstance(project)
                val godotPath = GodotProjectDiscoverer.getInstance(project).godotPath.value

                // Clean up old generated configurations
                val toRemove = runManager.allSettings.filter {
                    (it.type is ExeConfigurationType && it.name == RUN_CONFIGURATION_NAME
                            || it.type is DotNetExeConfigurationType && it.name == PROFILE_CONFIGURATION_NAME)
                        ||
                        (it.configuration is GodotDebugRunConfiguration
                            && (it.name == PLAYER_CONFIGURATION_NAME
                            ||it.name == EDITOR_CONFIGURATION_NAME
                            ||it.name == WAT_UNIT_TESTING)
                            && !File((it.configuration as GodotDebugRunConfiguration).parameters.exePath).exists())
                }
                for (value in toRemove) {
                    runManager.removeConfiguration(value)
                }

                // Add configuration, if it doesn't exist
                if (!runManager.allSettings.any { it.type is MonoRemoteConfigType && it.name == ATTACH_CONFIGURATION_NAME }) {
                    val configurationType = ConfigurationTypeUtil.findConfigurationType(MonoRemoteConfigType::class.java)
                    val runConfiguration = runManager.createConfiguration(ATTACH_CONFIGURATION_NAME, configurationType.factory)
                    val remoteConfig = runConfiguration.configuration as DotNetRemoteConfiguration
                    remoteConfig.port = godotDiscoverer.port
                    runConfiguration.storeInLocalWorkspace()
                    runManager.addConfiguration(runConfiguration)
                }

                if (!runManager.allSettings.any { it.type is GodotDebugRunConfigurationType && it.name == PLAYER_CONFIGURATION_NAME }) {
                    val configurationType = ConfigurationTypeUtil.findConfigurationType(GodotDebugRunConfigurationType::class.java)
                    val runConfiguration = runManager.createConfiguration(PLAYER_CONFIGURATION_NAME, configurationType.factory)
                    val config = runConfiguration.configuration as GodotDebugRunConfiguration
                    config.parameters.exePath = godotPath ?: ""
                    config.parameters.programParameters = "--path \"${project.basePath}\""
                    config.parameters.workingDirectory = "${project.basePath}"
                    runConfiguration.storeInLocalWorkspace()
                    runManager.addConfiguration(runConfiguration)
                }

                if (!runManager.allSettings.any { it.type is GodotDebugRunConfigurationType && it.name == EDITOR_CONFIGURATION_NAME }) {
                    val configurationType = ConfigurationTypeUtil.findConfigurationType(GodotDebugRunConfigurationType::class.java)
                    val runConfiguration = runManager.createConfiguration(EDITOR_CONFIGURATION_NAME, configurationType.factory)
                    val config = runConfiguration.configuration as GodotDebugRunConfiguration
                    config.parameters.exePath = godotPath ?: ""
                    config.parameters.programParameters = "--path \"${project.basePath}\" --editor"
                    config.parameters.workingDirectory = "${project.basePath}"
                    runConfiguration.storeInLocalWorkspace()
                    runManager.addConfiguration(runConfiguration)
                }

                if (godotDiscoverer.isGodotUnitTesting.value && !runManager.allSettings.any { it.type is GodotDebugRunConfigurationType && it.name == WAT_UNIT_TESTING }) {
                    val configurationType = ConfigurationTypeUtil.findConfigurationType(GodotDebugRunConfigurationType::class.java)
                    val runConfiguration = runManager.createConfiguration(WAT_UNIT_TESTING, configurationType.factory)
                    val config = runConfiguration.configuration as GodotDebugRunConfiguration
                    config.parameters.exePath = godotPath ?: ""
                    config.parameters.programParameters = "--path \"${project.basePath}\" \"res://addons/WAT/gui.tscn\""
                    config.parameters.workingDirectory = "${project.basePath}"
                    runConfiguration.storeInLocalWorkspace()
                    runManager.addConfiguration(runConfiguration)
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
}