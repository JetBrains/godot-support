package com.jetbrains.rider.plugins.godot.ui

import com.intellij.execution.RunManager
import com.intellij.execution.configurations.ConfigurationTypeUtil
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.project.Project
import com.jetbrains.rider.plugins.godot.run.GodotRunConfigurationGenerator
import com.jetbrains.rider.run.configurations.dotNetExe.DotNetExeConfiguration
import com.jetbrains.rider.run.configurations.dotNetExe.DotNetExeConfigurationType
import com.jetbrains.rider.run.createEmptyConsoleCommandLine
import com.jetbrains.rider.run.withRawParameters


open class  StartGodotEditorAction : DumbAwareAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return

        startEditor(project)
    }

    companion object {
        private val logger = Logger.getInstance(StartGodotEditorAction::class.java)
        fun startEditor(project: Project) {
            val runManager = RunManager.getInstance(project)
            val configurationType = ConfigurationTypeUtil.findConfigurationType(DotNetExeConfigurationType::class.java)
            val runConfiguration =
                runManager.findConfigurationByTypeAndName(configurationType.id, GodotRunConfigurationGenerator.EDITOR_CONFIGURATION_NAME)

            val exeConfiguration = runConfiguration?.configuration as DotNetExeConfiguration
            val runCommandLine = createEmptyConsoleCommandLine(exeConfiguration.parameters.useExternalConsole)
                .withEnvironment(exeConfiguration.parameters.envs)
                .withParentEnvironmentType(if (exeConfiguration.parameters.isPassParentEnvs) {
                    GeneralCommandLine.ParentEnvironmentType.CONSOLE
                } else {
                    GeneralCommandLine.ParentEnvironmentType.NONE
                })
                .withExePath(exeConfiguration.parameters.exePath)
                .withWorkDirectory(exeConfiguration.parameters.workingDirectory)
                .withRawParameters(exeConfiguration.parameters.programParameters)

            logger.info("Starting $runCommandLine")

            runCommandLine.createProcess()
        }
    }
}