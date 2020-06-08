package com.jetbrains.rider.plugins.godot.run.configurations

import com.intellij.execution.ExecutionBundle
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Comparing
import com.intellij.openapi.util.SystemInfo
import com.intellij.openapi.util.io.FileUtil
import com.jetbrains.rd.util.lifetime.Lifetime
import com.jetbrains.rider.run.configurations.LifetimedSettingsEditor
import com.jetbrains.rider.run.configurations.controls.*
import com.jetbrains.rider.run.configurations.exe.ExeConfigurationViewModel
import javax.swing.JComponent

class GodotDebugRunConfigurationEditor(private val project: Project) : LifetimedSettingsEditor<GodotDebugRunConfiguration>() {

    private lateinit var viewModel: ExeConfigurationViewModel

    override fun resetEditorFrom(configuration: GodotDebugRunConfiguration) {
        configuration.exeConfigurationParameters.apply {
            viewModel.reset(
                exePath,
                programParameters,
                workingDirectory,
                envs,
                isPassParentEnvs,
                useExternalConsole
            )
        }
    }

    override fun applyEditorTo(configuration: GodotDebugRunConfiguration) {
        configuration.exeConfigurationParameters.apply {
            exePath = FileUtil.toSystemIndependentName(viewModel.exePathSelector.path.value)
            programParameters = viewModel.programParametersEditor.parametersString.value
            workingDirectory = FileUtil.toSystemIndependentName(viewModel.workingDirectorySelector.path.value)
            envs = viewModel.environmentVariablesEditor.envs.value
            isPassParentEnvs = viewModel.environmentVariablesEditor.isPassParentEnvs.value
            useExternalConsole = viewModel.useExternalConsoleEditor.isSelected.value
        }
    }

    override fun createEditor(lifetime: Lifetime): JComponent {
        viewModel = ExeConfigurationViewModel(
            lifetime,
            PathSelector("Exe path:", FileChooserDescriptor(true, false, false,
                    false, false, false).withFileFilter { file ->
                Comparing.equal(file.extension, "exe", SystemInfo.isFileSystemCaseSensitive)
                        || Comparing.equal(file.extension, "dll", SystemInfo.isFileSystemCaseSensitive)
            }, lifetime),
            ProgramParametersEditor(ExecutionBundle.message("run.configuration.program.parameters"), lifetime),
            PathSelector("Working directory:", FileChooserDescriptorFactory.createSingleFolderDescriptor(), lifetime),
            EnvironmentVariablesEditor("Environment variables:"),
            FlagEditor("Use external console"),
            true
        )

        return ControlViewBuilder(lifetime, project).build(viewModel)
    }
}