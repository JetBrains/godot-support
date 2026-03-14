package com.jetbrains.rider.plugins.godot.actions

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.terminal.ui.TerminalWidget
import com.jetbrains.rider.plugins.godot.GodotPluginBundle
import com.jetbrains.rider.projectView.solutionDirectoryPath
import org.jetbrains.plugins.terminal.TerminalToolWindowManager
import kotlin.io.path.exists
import kotlin.io.path.pathString
import kotlin.io.path.useLines

class GenerateGodotSolutionAction : AnAction(), DumbAware {
    private var isVisible: Boolean? = null

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

    fun isVisible(project: Project): Boolean {
        if (isVisible != null) return isVisible!!
        val result = isPossibleToGenerateSolution(project)
        isVisible = result
        return result
    }

    private fun isPossibleToGenerateSolution(project: Project): Boolean {
        val folder = project.solutionDirectoryPath
        val sconsFile = folder.resolve("SConstruct")
        if (!sconsFile.exists()) return false

        return sconsFile.useLines { lines ->
            lines.any { it.trimStart().startsWith("opts.Add(BoolVariable(\"vsproj\"") }
        }
    }

    override fun update(e: AnActionEvent) {
        val project = e.project ?: return
        e.presentation.isEnabledAndVisible = isVisible(project) ?: false
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
