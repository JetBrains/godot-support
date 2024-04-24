package com.jetbrains.rider.plugins.godot.run

import com.intellij.execution.RunManager
import com.intellij.execution.configurations.ConfigurationTypeUtil
import com.intellij.openapi.client.ClientProjectSession
import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.jetbrains.rd.platform.util.idea.LifetimedService
import com.jetbrains.rd.protocol.SolutionExtListener
import com.jetbrains.rd.util.lifetime.Lifetime
import com.jetbrains.rd.util.reactive.adviseNotNull
import com.jetbrains.rd.util.reactive.viewNotNull
import com.jetbrains.rd.util.reactive.whenTrue
import com.jetbrains.rider.model.godot.frontendBackend.GodotFrontendBackendModel
import com.jetbrains.rider.plugins.godot.GodotProjectDiscoverer
import com.jetbrains.rider.plugins.godot.run.configurations.GodotDebugRunConfiguration
import com.jetbrains.rider.plugins.godot.run.configurations.GodotDebugRunConfigurationType
import com.jetbrains.rider.projectView.solution
import com.jetbrains.rider.projectView.solutionDirectory
import com.jetbrains.rider.run.configurations.dotNetExe.DotNetExeConfiguration
import com.jetbrains.rider.run.configurations.dotNetExe.DotNetExeConfigurationType
import com.jetbrains.rider.run.configurations.exe.ExeConfiguration
import com.jetbrains.rider.run.configurations.exe.ExeConfigurationType
import com.jetbrains.rider.run.configurations.remote.DotNetRemoteConfiguration
import com.jetbrains.rider.run.configurations.remote.MonoRemoteConfigType
import com.jetbrains.rider.runtime.dotNetCore.DotNetCoreRuntimeType
import java.nio.file.Paths
import kotlin.io.path.pathString
import kotlin.io.path.relativeToOrSelf

@Service
class GodotRunConfigurationGenerator : LifetimedService() {

    companion object {
        const val ATTACH_CONFIGURATION_NAME = "Attach to Player"

        const val PLAYER_CONFIGURATION_NAME = "Player"
        const val EDITOR_CONFIGURATION_NAME = "Editor"
    }

    class ProtocolListener : SolutionExtListener<GodotFrontendBackendModel> {
        private val logger = Logger.getInstance(GodotRunConfigurationGenerator::class.java)

        override fun extensionCreated(lifetime: Lifetime, session: ClientProjectSession, model: GodotFrontendBackendModel) {
            val project = session.project
            project.solution.isLoaded.whenTrue(lifetime){
                val godotDiscoverer = GodotProjectDiscoverer.getInstance(project)
                godotDiscoverer.godotDescriptor.viewNotNull(lifetime) { lt, descriptor ->
                    logger.info("descriptor = $descriptor")
                    val tempRelPath = Paths.get(descriptor.mainProjectBasePath).relativeToOrSelf(project.solutionDirectory.toPath())
                    val relPath = if (tempRelPath.pathString.isEmpty()) "./" else tempRelPath
                    val runManager = RunManager.getInstance(project)

                    GodotProjectDiscoverer.getInstance(project).godot4Path.advise(lt) { corePath->
                        if (corePath != null) {
                            val toRemove = runManager.allSettings.filter {
                                it.type is MonoRemoteConfigType && it.name == ATTACH_CONFIGURATION_NAME
                            }
                            for (value in toRemove) {
                                runManager.removeConfiguration(value)
                            }
                        }
                        else if (!descriptor.isPureGdScriptProject){
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

                    GodotProjectDiscoverer.getInstance(project).godot3Path.adviseNotNull(lt) { path ->
                        if (descriptor.isPureGdScriptProject){
                            createOrUpdateGdScriptRunConfiguration(PLAYER_CONFIGURATION_NAME, "--path \"${relPath}\"", runManager, path, project)
                            createOrUpdateGdScriptRunConfiguration(EDITOR_CONFIGURATION_NAME, "--path \"${relPath}\" --editor", runManager, path, project)
                            return@adviseNotNull
                        }
                        createOrUpdateRunConfiguration(PLAYER_CONFIGURATION_NAME, "--path \"${relPath}\"", runManager, path, project)
                        createOrUpdateRunConfiguration(EDITOR_CONFIGURATION_NAME, "--path \"${relPath}\" --editor", runManager, path, project)
                    }
                    GodotProjectDiscoverer.getInstance(project).godot4Path.adviseNotNull(lt) { path ->
                        if (descriptor.isPureGdScriptProject){
                            createOrUpdateGdScriptRunConfiguration(PLAYER_CONFIGURATION_NAME, "--path \"${relPath}\"", runManager, path, project)
                            createOrUpdateGdScriptRunConfiguration(EDITOR_CONFIGURATION_NAME, "--path \"${relPath}\" --editor", runManager, path, project)
                            return@adviseNotNull
                        }
                        createOrUpdateCoreRunConfiguration(PLAYER_CONFIGURATION_NAME, "--path \"${relPath}\"", runManager, path, project)
                        createOrUpdateCoreRunConfiguration(EDITOR_CONFIGURATION_NAME, "--path \"${relPath}\" --editor", runManager, path, project)
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
            val configs = runManager.allSettings.filter { it.type is DotNetExeConfigurationType && it.name == configurationName }
            if (configs.any()) {
                configs.forEach{
                    (it.configuration as DotNetExeConfiguration).parameters.exePath = godotPath
                    (it.configuration as DotNetExeConfiguration).parameters.workingDirectory = project.solutionDirectory.absolutePath
                }
            } else {
                val configurationType = ConfigurationTypeUtil.findConfigurationType(DotNetExeConfigurationType::class.java)
                val runConfiguration = runManager.createConfiguration(configurationName, configurationType.factory)
                val config = runConfiguration.configuration as DotNetExeConfiguration
                config.parameters.exePath = godotPath
                config.parameters.programParameters = programParameters
                config.parameters.workingDirectory = project.solutionDirectory.absolutePath
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
                    (it.configuration as GodotDebugRunConfiguration).parameters.workingDirectory = project.solutionDirectory.absolutePath
                }
            } else {
                val configurationType = ConfigurationTypeUtil.findConfigurationType(GodotDebugRunConfigurationType::class.java)
                val runConfiguration = runManager.createConfiguration(configurationName, configurationType.factory)
                val config = runConfiguration.configuration as GodotDebugRunConfiguration
                config.parameters.exePath = godotPath
                config.parameters.programParameters = programParameters
                config.parameters.workingDirectory = project.solutionDirectory.absolutePath
                runConfiguration.storeInLocalWorkspace()
                runManager.addConfiguration(runConfiguration)
            }
        }

        private fun createOrUpdateGdScriptRunConfiguration(
            configurationName: String,
            programParameters:String,
            runManager: RunManager,
            godotPath: String,
            project: Project
        ) {
            val configs = runManager.allSettings.filter { it.type is ExeConfigurationType && it.name == configurationName }
            if (configs.any()) {
                configs.forEach{
                    (it.configuration as ExeConfiguration).parameters.exePath = godotPath
                    (it.configuration as ExeConfiguration).parameters.workingDirectory = project.solutionDirectory.absolutePath
                }
            } else {
                val configurationType = ConfigurationTypeUtil.findConfigurationType(ExeConfigurationType::class.java)
                val runConfiguration = runManager.createConfiguration(configurationName, configurationType.factory)
                val config = runConfiguration.configuration as ExeConfiguration
                config.parameters.exePath = godotPath
                config.parameters.programParameters = programParameters
                config.parameters.workingDirectory = project.solutionDirectory.absolutePath
                config.beforeRunTasks.removeIf { true }
                runConfiguration.storeInLocalWorkspace()
                runManager.addConfiguration(runConfiguration)
            }
        }
    }
}