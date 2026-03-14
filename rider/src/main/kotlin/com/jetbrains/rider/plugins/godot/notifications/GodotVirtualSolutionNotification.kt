package com.jetbrains.rider.plugins.godot.notifications

import com.intellij.notification.Notification
import com.intellij.notification.NotificationAction
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.EDT
import com.intellij.openapi.application.writeAction
import com.intellij.openapi.application.writeIntentReadAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ex.ProjectManagerEx
import com.intellij.openapi.startup.ProjectActivity
import com.intellij.openapi.vfs.refreshAndFindVirtualFile
import com.jetbrains.rd.ide.model.RdVirtualSolution
import com.jetbrains.rd.platform.protocol.EdtScheduler
import com.jetbrains.rd.util.lifetime.Lifetime
import com.jetbrains.rd.util.reactive.whenTrue
import com.jetbrains.rd.util.threading.coroutines.launch
import com.jetbrains.rider.plugins.godot.GodotPluginBundle
import com.jetbrains.rider.plugins.godot.GodotProjectLifetimeService
import com.jetbrains.rider.plugins.godot.actions.GenerateGodotSolutionAction
import com.jetbrains.rider.projectView.SolutionManager
import com.jetbrains.rider.projectView.solution
import com.jetbrains.rider.projectView.solutionDescription
import com.jetbrains.rider.projectView.solutionDirectoryPath
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.io.path.exists
import kotlin.io.path.nameWithoutExtension

class GodotVirtualSolutionNotification : ProjectActivity {

    companion object {
        private const val GROUP_ID = "GodotVirtualSolutionNotificationGroupId"
    }

    override suspend fun execute(project: Project) {
        withContext(Dispatchers.EDT) {
            if (project.isDisposed || project.isDefault) return@withContext

            val lifetime = GodotProjectLifetimeService.getLifetime(project)
            project.solution.isLoaded.whenTrue(lifetime) { _ ->
                val solutionDescription = project.solutionDescription
                if (solutionDescription is RdVirtualSolution
                    && (ActionManager.getInstance().getAction("Godot.GenerateSolution") as GenerateGodotSolutionAction)
                        .isVisible(project) ?: false
                ) {
                    showNotification(project)
                }
            }
        }
    }

    private fun showNotification(project: Project) {
        val title = GodotPluginBundle.message("notification.title.godot.virtual.solution")
        val content = GodotPluginBundle.message("notification.content.godot.generate.sln.with.scons")
        val notification = Notification(
            GROUP_ID,
            title,
            content,
            NotificationType.INFORMATION
        )

        notification.addAction(object : NotificationAction(
            GodotPluginBundle.message("action.Godot.GenerateSolution.ShortName.text")
        ) {
            override fun actionPerformed(e: AnActionEvent, notification: Notification) {
                ActionManager.getInstance().getAction("Godot.GenerateSolution").actionPerformed(e)
            }
        })

        notification.addAction(object : NotificationAction(GodotPluginBundle.message("open.solution.action.text")) {
            override fun actionPerformed(e: AnActionEvent, notification: Notification) {
                val sf = project.solutionDirectoryPath.resolve("godot.sln").refreshAndFindVirtualFile() ?: return
                // SolutionManager doesn't close the current project if focusOpenInNewFrame is set to true,
                // and if it's set to false, we get prompted if we want to open in new or same frame. We
                // don't care - we want to close this project, so new frame or reusing means nothing
                Lifetime.Eternal.launch(Dispatchers.Default) {
                    writeIntentReadAction {
                        e.project?.let { ProjectManagerEx.getInstanceEx().closeAndDispose(it) }
                    }
                }
                Lifetime.Eternal.launch(Dispatchers.Default) {
                    SolutionManager.openExistingSolution(null, true, sf, false, true)
                }
            }
        })

        notification.setSuggestionType(true)
        notification.notify(project)
    }
}
