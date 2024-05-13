package com.jetbrains.rider.plugins.godot.actions

import com.intellij.execution.RunManager
import com.intellij.execution.configurations.ConfigurationTypeUtil
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.project.Project
import com.jetbrains.rider.plugins.godot.run.GodotRunConfigurationGenerator
import com.jetbrains.rider.plugins.godot.run.configurations.GodotDebugRunConfiguration
import com.jetbrains.rider.plugins.godot.run.configurations.GodotDebugRunConfigurationType
import com.jetbrains.rider.run.configurations.dotNetExe.DotNetExeConfiguration
import com.jetbrains.rider.run.configurations.dotNetExe.DotNetExeConfigurationType
import com.jetbrains.rider.run.createEmptyConsoleCommandLine
import com.jetbrains.rider.run.withRawParameters


class  StartGodotEditorAction : DumbAwareAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return

        startEditor(project)
    }

    companion object {
        private val logger = Logger.getInstance(StartGodotEditorAction::class.java)

        data class Parameters(val useExternalConsole:Boolean, val envs: Map<String, String>, val isPassParentEnvs:Boolean, val exePath: String,
                              val workingDirectory:String, val programParameters:String)
        fun startEditor(project: Project) {
            val runManager = RunManager.getInstance(project)
            val configurationType = ConfigurationTypeUtil.findConfigurationType(DotNetExeConfigurationType::class.java)
            var runConfiguration =
                runManager.findConfigurationByTypeAndName(
                    configurationType.id,
                    GodotRunConfigurationGenerator.EDITOR_CONFIGURATION_NAME
                )

            if (runConfiguration == null)
                runConfiguration = runManager.findConfigurationByTypeAndName(
                    ConfigurationTypeUtil.findConfigurationType(GodotDebugRunConfigurationType::class.java).id,
                    GodotRunConfigurationGenerator.EDITOR_CONFIGURATION_NAME
                )

            if (runConfiguration == null)
                throw Exception("Godot ${GodotRunConfigurationGenerator.EDITOR_CONFIGURATION_NAME} run configuration was not present.")

            val parameters = when (val exeConfiguration = runConfiguration.configuration) {
                is DotNetExeConfiguration -> Parameters(
                    exeConfiguration.parameters.useExternalConsole,
                    exeConfiguration.parameters.envs,
                    exeConfiguration.parameters.isPassParentEnvs,
                    exeConfiguration.parameters.exePath,
                    exeConfiguration.parameters.workingDirectory,
                    exeConfiguration.parameters.programParameters
                )
                is GodotDebugRunConfiguration -> Parameters(
                    exeConfiguration.parameters.useExternalConsole,
                    exeConfiguration.parameters.envs,
                    exeConfiguration.parameters.isPassParentEnvs,
                    exeConfiguration.parameters.exePath,
                    exeConfiguration.parameters.workingDirectory,
                    exeConfiguration.parameters.programParameters
                )
                else -> throw Exception("Unexpected run configuration type")
            }

            val runCommandLine = createEmptyConsoleCommandLine(parameters.useExternalConsole)
                .withEnvironment(parameters.envs)
                .withParentEnvironmentType(if (parameters.isPassParentEnvs) {
                    GeneralCommandLine.ParentEnvironmentType.CONSOLE
                } else {
                    GeneralCommandLine.ParentEnvironmentType.NONE
                })
                .withExePath(parameters.exePath)
                .withWorkDirectory(parameters.workingDirectory)
                .withRawParameters(parameters.programParameters)

            logger.info("Starting $runCommandLine")

            runCommandLine.createProcess()
        }
    }
}