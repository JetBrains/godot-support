package com.jetbrains.rider.plugins.godot.lang.service

import com.intellij.notification.Notification
import com.intellij.notification.NotificationAction
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.jetbrains.rd.platform.protocol.EdtScheduler
import com.jetbrains.rd.util.lifetime.SequentialLifetimes
import com.jetbrains.rd.util.threading.coroutines.launch
import com.jetbrains.rider.plugins.godot.GodotPluginBundle
import com.jetbrains.rider.plugins.godot.GodotProjectLifetimeService
import com.jetbrains.rider.plugins.godot.actions.StartGodotEditorAction

@Service(Service.Level.PROJECT)
class GodotLspNotification(val project: Project) {

    companion object {
        private const val GROUP_ID = "GodotSupportNotificationGroupId"
        
        fun getService(project: Project): GodotLspNotification = project.service<GodotLspNotification>()
    }

    private val pluginLifetime = GodotProjectLifetimeService.getLifetime(project)
    private val nestedLifetimeDef = pluginLifetime.createNested()
    private val sequentialLifetimes = SequentialLifetimes(nestedLifetimeDef)

    /**
     * Shows a warning notification when LSP attempts to connect to a non-matching project
     */
    fun showNonMatchingProjectWarning() {
        val title = GodotPluginBundle.message("notification.title.godot.lsp.warning")
        val content = GodotPluginBundle.message("notification.content.lsp.attempted.to.connect.to.non.matching.project")
        val notificationLifetime = sequentialLifetimes.next()

        val notification = Notification(
            GROUP_ID,
            title,
            content,
            NotificationType.WARNING
        )

        notification.addAction(object : NotificationAction(
            GodotPluginBundle.message("action.StartEditorAction.text")) {
            override fun actionPerformed(e: AnActionEvent, notification: Notification) {
                StartGodotEditorAction.startEditor(project)
                notificationLifetime.terminate()
            }
        })

        notificationLifetime.launch(EdtScheduler) {
            Notifications.Bus.notify(notification, project)
            notificationLifetime.onTermination { notification.expire() }
        }
    }
}