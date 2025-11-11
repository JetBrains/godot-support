package com.jetbrains.rider.plugins.godot.notifications

import com.intellij.notification.Notification
import com.intellij.notification.NotificationAction
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.EDT
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ex.ProjectManagerEx
import com.intellij.openapi.startup.ProjectActivity
import com.intellij.terminal.ui.TerminalWidget
import com.jetbrains.rd.ide.model.RdVirtualSolution
import com.jetbrains.rd.platform.protocol.EdtScheduler
import com.jetbrains.rd.util.lifetime.Lifetime
import com.jetbrains.rd.util.reactive.whenTrue
import com.jetbrains.rd.util.threading.coroutines.launch
import com.jetbrains.rdclient.util.idea.toVirtualFile
import com.jetbrains.rider.plugins.godot.GodotPluginBundle
import com.jetbrains.rider.plugins.godot.GodotProjectLifetimeService
import com.jetbrains.rider.projectView.SolutionManager
import com.jetbrains.rider.projectView.solution
import com.jetbrains.rider.projectView.solutionDescription
import com.jetbrains.rider.projectView.solutionDirectory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.plugins.terminal.TerminalToolWindowManager

class GodotVirtualSolutionNotification : ProjectActivity {

    companion object {
        private const val GROUP_ID = "GodotVirtualSolutionNotificationGroupId"
        private val LOG = logger<GodotVirtualSolutionNotification>()
    }

    override suspend fun execute(project: Project) {
        withContext(Dispatchers.EDT) {
            if (project.isDisposed || project.isDefault) return@withContext

            val lifetime = GodotProjectLifetimeService.getLifetime(project)
            project.solution.isLoaded.whenTrue(lifetime) { lt ->
                val solutionDescription = project.solutionDescription
                if (solutionDescription is RdVirtualSolution
                    && project.solutionDirectory.nameWithoutExtension.equals("godot", true)
                    && !project.solutionDirectory.resolve("project.godot").exists()
                ) {
                    showNotification(lt, project)
                }
            }
        }
    }

    private fun showNotification(lt: Lifetime, project: Project) {
        val title = GodotPluginBundle.message("notification.title.godot.virtual.solution")
        val content = GodotPluginBundle.message("notification.content.godot.generate.sln.with.scons")
        val notification = Notification(
            GROUP_ID,
            title,
            content,
            NotificationType.INFORMATION
        )
        val solutionFile = project.solutionDirectory.resolve("godot.sln").toVirtualFile(false)
        if (solutionFile == null) {
            notification.addAction(object : NotificationAction(
                GodotPluginBundle.message("action.godot.generate.sln")) {
                override fun actionPerformed(e: AnActionEvent, notification: Notification) {
                    runScons(project)
                }
            })
        }

        notification.addAction(object : NotificationAction(
            GodotPluginBundle.message("open.solution.action.text")) {
            override fun actionPerformed(e: AnActionEvent, notification: Notification) {
                val sf = project.solutionDirectory.resolve("godot.sln").toVirtualFile(true) ?: return
                // SolutionManager doesn't close the current project if focusOpenInNewFrame is set to true,
                // and if it's set to false, we get prompted if we want to open in new or same frame. We
                // don't care - we want to close this project, so new frame or reusing means nothing
                Lifetime.Eternal.launch(EdtScheduler) {
                    e.project?.let { ProjectManagerEx.getInstanceEx().closeAndDispose(it) }
                }
                Lifetime.Eternal.launch {
                    SolutionManager.openExistingSolution(null, true, sf, false, true)
                }
            }
        })

        notification.setSuggestionType(true)
        notification.notify(project)
    }

    private fun runScons(project: Project) {
        val terminalManager = TerminalToolWindowManager.getInstance(project)
        val workingDirectory = project.solutionDirectory.path

        val terminalWidget: TerminalWidget =
            terminalManager.createShellWidget(
                workingDirectory,
                GodotPluginBundle.message("action.godot.generate.sln"),
                true,
                false,
            )

        terminalManager.toolWindow?.show(null)
        terminalWidget.sendCommandToExecute("scons vsproj=yes dev_build=yes")
    }
}
