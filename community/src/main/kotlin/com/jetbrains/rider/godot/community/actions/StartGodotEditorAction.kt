package com.jetbrains.rider.godot.community.actions

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.project.Project
import com.jetbrains.rider.godot.community.utils.GodotCommunityUtil

object StartGodotEditorAction : DumbAwareAction() {
    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        startEditor(project)
    }

    override fun update(e: AnActionEvent) {
        val project = e.project
        if (project == null) {
            e.presentation.isEnabledAndVisible = false
            return
        }

        val isGodotProject = GodotCommunityUtil.isGodotProject(project)
        val executableAvailable = GodotCommunityUtil.getGodotExecutablePath(project) != null

        e.presentation.isVisible = isGodotProject
        e.presentation.isEnabled = executableAvailable

        super.update(e)
    }

    fun startEditor(project: Project) {
        val launchConfig = GodotCommunityUtil.getEditorLaunchConfig(project) ?: return

        val runCommandLine = GeneralCommandLine(launchConfig.executablePath.toString())
            .withEnvironment(launchConfig.environmentVariables)
            .withParentEnvironmentType(
                if (launchConfig.isPassParentEnvs) {
                    GeneralCommandLine.ParentEnvironmentType.CONSOLE
                } else {
                    GeneralCommandLine.ParentEnvironmentType.NONE
                }
            )
            .withWorkingDirectory(launchConfig.workingDirectory)
            .withParameters(launchConfig.arguments)

        logger.info("Starting Godot editor: ${runCommandLine.commandLineString}")

        // without discarding output, closing GodotEditor on mac would take several minutes
        runCommandLine.toProcessBuilder()
            .redirectError(ProcessBuilder.Redirect.DISCARD)
            .redirectOutput(ProcessBuilder.Redirect.DISCARD)
            .start()
    }

    private val logger = Logger.getInstance(StartGodotEditorAction::class.java)
}
