package com.jetbrains.rider.plugins.godot.run

import com.intellij.execution.ProgramRunnerUtil
import com.intellij.execution.RunManager
import com.intellij.execution.configurations.ConfigurationTypeUtil
import com.intellij.execution.executors.DefaultDebugExecutor
import com.intellij.openapi.project.Project
import com.jetbrains.rider.run.configurations.dotNetExe.DotNetExeConfiguration
import com.jetbrains.rider.run.configurations.dotNetExe.DotNetExeConfigurationType

class  RunChickenTestsUtil {

    companion object {
        fun execute(testIdentifier: String, project: Project) {
            val runManager = RunManager.Companion.getInstance(project)

            val configurationType = ConfigurationTypeUtil.findConfigurationType(DotNetExeConfigurationType::class.java)
            val runConfiguration = runManager.findConfigurationByTypeAndName(
                configurationType.id, GodotRunConfigurationGenerator.EDITOR_CONFIGURATION_NAME)

            if (runConfiguration == null)
                throw Exception("Godot ${GodotRunConfigurationGenerator.EDITOR_CONFIGURATION_NAME} run configuration was not present.")

            val configuration = runConfiguration.configuration as DotNetExeConfiguration
            val programParameters = configuration.parameters.programParameters
                .replace(" --editor", "") + " --run-tests=$testIdentifier --quit-on-finish"
            val testsConfig = GodotRunConfigurationGenerator.ProtocolListener.createOrUpdateCoreRunConfiguration(
                GodotRunConfigurationGenerator.Companion.CHICKENSOFT_TEST_CONFIGURATION_NAME,
                programParameters,
                runManager,
                configuration.parameters.exePath,
                project)
            // createOrUpdateCoreRunConfiguration doesn't update programParameters...
            (testsConfig.configuration as DotNetExeConfiguration).parameters.programParameters = programParameters
            runManager.selectedConfiguration = testsConfig
            ProgramRunnerUtil.executeConfiguration(testsConfig, DefaultDebugExecutor.getDebugExecutorInstance())
        }
    }
}