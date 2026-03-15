package com.jetbrains.rider.plugins.godot.cpp.actions

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.terminal.ui.TerminalWidget
import com.jetbrains.rider.plugins.godot.GodotPluginBundle
import com.jetbrains.rider.plugins.godot.cpp.GodotEngineSolutionAvailability
import com.jetbrains.rider.projectView.solutionDirectoryPath
import org.jetbrains.plugins.terminal.TerminalToolWindowManager
import kotlin.io.path.pathString

class GodotEngineGenerateSolutionAction : AnAction(), DumbAware {
    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

    override fun update(e: AnActionEvent) {
        val project = e.project ?: return
        e.presentation.isEnabledAndVisible = GodotEngineSolutionAvailability.getInstance(project).isAvailable()
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        runScons(project)
    }

    private fun runScons(project: Project) {
        val terminalManager = TerminalToolWindowManager.getInstance(project)
        val workingDirectory = project.solutionDirectoryPath

        val terminalWidget: TerminalWidget =
            terminalManager.createShellWidget(
                workingDirectory.pathString,
                GodotPluginBundle.message("action.Godot.GenerateSolution.text"),
                true,
                false,
            )

        terminalManager.toolWindow?.show(null)
        terminalWidget.sendCommandToExecute("scons vsproj=yes dev_build=yes")
    }
}
