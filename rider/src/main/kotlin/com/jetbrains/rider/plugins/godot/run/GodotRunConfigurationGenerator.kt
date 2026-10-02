package com.jetbrains.rider.plugins.godot.run

import com.intellij.execution.RunManager
import com.intellij.execution.RunnerAndConfigurationSettings
import com.intellij.execution.configurations.ConfigurationTypeUtil
import com.intellij.openapi.client.ClientProjectSession
import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.jetbrains.rd.platform.util.idea.LifetimedService
import com.jetbrains.rd.protocol.SolutionExtListener
import com.jetbrains.rd.util.lifetime.Lifetime
import com.jetbrains.rd.util.reactive.viewNotNull
import com.jetbrains.rd.util.reactive.whenTrue
import com.jetbrains.rider.ijent.extensions.toNioPath
import com.jetbrains.rider.model.godot.frontendBackend.GodotFrontendBackendModel
import com.jetbrains.rider.plugins.godot.GodotPluginBundle
import com.jetbrains.rider.plugins.godot.GodotProjectDiscoverer
import com.jetbrains.rider.plugins.godot.run.configurations.GodotDebugRunConfiguration
import com.jetbrains.rider.plugins.godot.run.configurations.GodotDebugRunConfigurationType
import com.jetbrains.rider.plugins.godot.run.configurations.GodotDebugRunCurrentScene
import com.jetbrains.rider.plugins.godot.run.configurations.GodotDebugRunCurrentSceneType
import com.jetbrains.rider.plugins.godot.run.configurations.GodotInEditorDebugRunConfigurationType
import com.jetbrains.rider.projectView.isCMakeSolution
import com.jetbrains.rider.projectView.solution
import com.jetbrains.rider.projectView.solutionDirectoryPath
import com.jetbrains.rider.run.configurations.dotNetExe.DotNetExeConfiguration
import com.jetbrains.rider.run.configurations.dotNetExe.DotNetExeConfigurationType
import com.jetbrains.rider.run.configurations.exe.ExeConfiguration
import com.jetbrains.rider.run.configurations.exe.ExeConfigurationType
import com.jetbrains.rider.run.configurations.remote.DotNetRemoteConfiguration
import com.jetbrains.rider.run.configurations.remote.MonoRemoteConfigType
import com.jetbrains.rider.runtime.dotNetCore.DotNetCoreRuntimeType
import org.jetbrains.annotations.NonNls
import kotlin.io.path.pathString
import kotlin.io.path.relativeToOrSelf

private val logger = Logger.getInstance(GodotRunConfigurationGenerator::class.java)

@Service
class GodotRunConfigurationGenerator : LifetimedService() {

    companion object {
        @NonNls
        const val ATTACH_CONFIGURATION_NAME: String = "Attach to Player"

        @NonNls
        const val PLAYER_CONFIGURATION_NAME: String = "Player"

        @NonNls
        const val EDITOR_CONFIGURATION_NAME: String = "Editor"

        @NonNls
        const val CHICKENSOFT_TEST_CONFIGURATION_NAME: String = "Debug test"
    }

    class ProtocolListener : SolutionExtListener<GodotFrontendBackendModel> {
        data class ProjectType(val isPureGdScriptProject: Boolean, val isCmakeProject: Boolean)

        fun generateConfigurations(
            godot3Path: String?,
            godot4Path: String?,
            godotPath: String?,
            runManager: RunManager,
            project: Project,
            relPath: String,
            projectType: ProjectType,
            port: Int,
            godotProjectPath: String,
        ) {
            if (godot3Path != null && godot4Path != null) {
                logger.warn("The Godot project has both Godot 3 and Godot 4 paths. Preferring Godot 4 for configuration generation.")
            }
            if (godot4Path != null) {
                removeMonoAttachConfiguration(runManager)
            }

            val supportsManagedDebugging = !projectType.isPureGdScriptProject && !projectType.isCmakeProject
            when {
                // Prefer godot4 over godot3 -> do not reorder
                godot4Path != null -> {
                    generateGodot4(godot4Path, runManager, project, relPath)
                    if (supportsManagedDebugging) {
                        createInEditorDebugRunConfiguration(runManager)
                    }
                    if (!projectType.isPureGdScriptProject) {
                        createOrUpdateCurrentSceneRunConfiguration(runManager, godot4Path, godotProjectPath)
                    }
                }
                godot3Path != null -> {
                    if (supportsManagedDebugging) {
                        createMonoAttachConfiguration(runManager, port)
                    }
                    generateGodot3(godot3Path, runManager, project, relPath)
                }
            }
            if (projectType.isPureGdScriptProject && godotPath != null) {
                generateGDScript(godotPath, runManager, project, relPath)
            }
            selectConfigurationIfNeeded(runManager)
        }

        private fun generateGodot4(
            corePath: String,
            runManager: RunManager,
            project: Project,
            relPath: String,
        ) {
            createOrUpdateCoreRunConfiguration(
                PLAYER_CONFIGURATION_NAME,
                "--path \"${relPath}\"",
                runManager,
                corePath,
                project
            )
            createOrUpdateCoreRunConfiguration(
                EDITOR_CONFIGURATION_NAME,
                "--path \"${relPath}\" --editor",
                runManager,
                corePath,
                project
            )
        }

        private fun generateGodot3(
            path: String,
            runManager: RunManager,
            project: Project,
            relPath: String,
        ) {
            createOrUpdateRunConfiguration(
                PLAYER_CONFIGURATION_NAME,
                "--path \"${relPath}\"",
                runManager,
                path,
                project
            )
            createOrUpdateRunConfiguration(
                EDITOR_CONFIGURATION_NAME,
                "--path \"${relPath}\" --editor",
                runManager,
                path,
                project
            )
        }

        private fun generateGDScript(path: String, runManager: RunManager, project: Project, relPath: String) {
            createOrUpdateNativeExecutableRunConfiguration(
                EDITOR_CONFIGURATION_NAME,
                "--path \"${relPath}\" --editor",
                runManager,
                path,
                project
            )
        }

        private fun createMonoAttachConfiguration(
            runManager: RunManager,
            port: Int,
        ) {
            if (!runManager.allSettings.any { it.type is MonoRemoteConfigType && it.name == ATTACH_CONFIGURATION_NAME }
            ) {
                val configurationType = ConfigurationTypeUtil.findConfigurationType(MonoRemoteConfigType::class.java)
                val runConfiguration = runManager.createConfiguration(ATTACH_CONFIGURATION_NAME, configurationType.factory)
                val remoteConfig = runConfiguration.configuration as DotNetRemoteConfiguration
                remoteConfig.port = port
                runConfiguration.storeInLocalWorkspace()
                runManager.addConfiguration(runConfiguration)
            }
        }

        private fun removeMonoAttachConfiguration(runManager: RunManager) {
            val toRemove = runManager.allSettings.filter {
                it.type is MonoRemoteConfigType && it.name == ATTACH_CONFIGURATION_NAME
            }
            for (value in toRemove) {
                runManager.removeConfiguration(value)
            }
        }

        override fun extensionCreated(lifetime: Lifetime, session: ClientProjectSession, model: GodotFrontendBackendModel) {
            val project = session.project
            project.solution.isLoaded.whenTrue(lifetime) {
                val godotDiscoverer = GodotProjectDiscoverer.getInstance(project)
                godotDiscoverer.godotDescriptor.viewNotNull(lifetime) { lt, descriptor ->
                    val tempRelPath = descriptor.mainProjectBasePath.toNioPath().relativeToOrSelf(project.solutionDirectoryPath)
                    val relPath = tempRelPath.pathString.ifEmpty { "./" }
                    val runManager = RunManager.getInstance(project)
                    val projectType = ProjectType(descriptor.isPureGdScriptProject, project.isCMakeSolution)
                    fun generate() {
                        generateConfigurations(
                            godotDiscoverer.godot3Path.value,
                            godotDiscoverer.godot4Path.value,
                            godotDiscoverer.godotPath.valueOrNull,
                            runManager,
                            project,
                            relPath,
                            projectType,
                            godotDiscoverer.port,
                            descriptor.mainProjectBasePath.toNioPath().pathString,
                        )
                    }
                    godotDiscoverer.godot4Path.advise(lt) { generate() }
                    godotDiscoverer.godot3Path.advise(lt) { generate() }
                    godotDiscoverer.godotPath.advise(lt) { generate() }
                }
            }
        }

        /**
         * The scene to run is known on launch only, see [GodotDebugRunCurrentScene.getRunProfileStateAsync],
         * the executable and the working directory are set here, so that the configuration is valid and
         * shows the actual values in the editor.
         */
        private fun createOrUpdateCurrentSceneRunConfiguration(
            runManager: RunManager,
            godotPath: String,
            godotProjectPath: String,
        ) {
            val configs = runManager.allSettings.filter {
                it.type is GodotDebugRunCurrentSceneType && it.name == GodotDebugRunCurrentScene.CONFIGURATION_NAME
            }
            if (configs.any()) {
                configs.forEach {
                    (it.configuration as GodotDebugRunCurrentScene).parameters.exePath = godotPath
                    (it.configuration as GodotDebugRunCurrentScene).parameters.workingDirectory = godotProjectPath
                }
            } else {
                val configurationType = ConfigurationTypeUtil.findConfigurationType(GodotDebugRunCurrentSceneType::class.java)
                val runConfiguration =
                    runManager.createConfiguration(GodotDebugRunCurrentScene.CONFIGURATION_NAME, configurationType.factory)
                val config = runConfiguration.configuration as GodotDebugRunCurrentScene
                config.parameters.exePath = godotPath
                config.parameters.workingDirectory = godotProjectPath
                runConfiguration.storeInLocalWorkspace()
                runManager.addConfiguration(runConfiguration)
            }
        }

        private fun createInEditorDebugRunConfiguration(runManager: RunManager) {
            if (runManager.allSettings.any { it.type is GodotInEditorDebugRunConfigurationType }) {
                return
            }

            val configurationType = ConfigurationTypeUtil.findConfigurationType(GodotInEditorDebugRunConfigurationType::class.java)
            val name = GodotPluginBundle.message("godot.debug.in.editor.configuration.name")
            val runConfiguration = runManager.createConfiguration(name, configurationType.factory)
            runConfiguration.storeInLocalWorkspace()
            runManager.addConfiguration(runConfiguration)
        }

        // make configuration selected if nothing is selected
        private fun selectConfigurationIfNeeded(runManager: RunManager) {
            if (runManager.selectedConfiguration == null) {
                val runConfiguration = runManager.findConfigurationByName(PLAYER_CONFIGURATION_NAME)
                if (runConfiguration != null) {
                    runManager.selectedConfiguration = runConfiguration
                }
            }
        }

        companion object {
            fun createOrUpdateCoreRunConfiguration(
                configurationName: String,
                programParameters: String,
                runManager: RunManager,
                godotPath: String,
                project: Project
            ): RunnerAndConfigurationSettings {
                val configs = runManager.allSettings.filter { it.type is DotNetExeConfigurationType && it.name == configurationName }
                if (configs.any()) {
                    configs.forEach {
                        (it.configuration as DotNetExeConfiguration).parameters.exePath = godotPath
                        (it.configuration as DotNetExeConfiguration).parameters.workingDirectory = project.solutionDirectoryPath.pathString
                    }
                    return configs.last()
                } else {
                    val configurationType = ConfigurationTypeUtil.findConfigurationType(DotNetExeConfigurationType::class.java)
                    val runConfiguration = runManager.createConfiguration(configurationName, configurationType.factory)
                    val config = runConfiguration.configuration as DotNetExeConfiguration
                    config.parameters.exePath = godotPath
                    config.parameters.programParameters = programParameters
                    config.parameters.workingDirectory = project.solutionDirectoryPath.pathString
                    config.parameters.runtimeType = DotNetCoreRuntimeType
                    runConfiguration.storeInLocalWorkspace()
                    runManager.addConfiguration(runConfiguration)
                    return runConfiguration
                }
            }
        }

        private fun createOrUpdateRunConfiguration(
            configurationName: String,
            programParameters: String,
            runManager: RunManager,
            godotPath: String,
            project: Project
        ) {
            val configs = runManager.allSettings.filter { it.type is GodotDebugRunConfigurationType && it.name == configurationName }
            if (configs.any()) {
                configs.forEach {
                    (it.configuration as GodotDebugRunConfiguration).parameters.exePath = godotPath
                    (it.configuration as GodotDebugRunConfiguration).parameters.workingDirectory = project.solutionDirectoryPath.pathString
                }
            } else {
                val configurationType = ConfigurationTypeUtil.findConfigurationType(GodotDebugRunConfigurationType::class.java)
                val runConfiguration = runManager.createConfiguration(configurationName, configurationType.factory)
                val config = runConfiguration.configuration as GodotDebugRunConfiguration
                config.parameters.exePath = godotPath
                config.parameters.programParameters = programParameters
                config.parameters.workingDirectory = project.solutionDirectoryPath.pathString
                runConfiguration.storeInLocalWorkspace()
                runManager.addConfiguration(runConfiguration)
            }
        }

        private fun createOrUpdateNativeExecutableRunConfiguration(
            configurationName: String,
            programParameters: String,
            runManager: RunManager,
            godotPath: String,
            project: Project
        ) {
            val configs = runManager.allSettings.filter { it.type is ExeConfigurationType && it.name == configurationName }
            if (configs.any()) {
                configs.forEach {
                    (it.configuration as ExeConfiguration).parameters.exePath = godotPath
                    (it.configuration as ExeConfiguration).parameters.workingDirectory = project.solutionDirectoryPath.pathString
                }
            } else {
                val configurationType = ConfigurationTypeUtil.findConfigurationType(ExeConfigurationType::class.java)
                val runConfiguration = runManager.createConfiguration(configurationName, configurationType.factory)
                val config = runConfiguration.configuration as ExeConfiguration
                config.parameters.exePath = godotPath
                config.parameters.programParameters = programParameters
                config.parameters.workingDirectory = project.solutionDirectoryPath.pathString
                config.beforeRunTasks = emptyList()
                runConfiguration.storeInLocalWorkspace()
                runManager.addConfiguration(runConfiguration)
            }
        }
    }
}
