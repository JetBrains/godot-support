package gdscript.lsp

import com.intellij.notification.Notification
import com.intellij.notification.NotificationAction
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.EDT
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.jetbrains.rd.util.lifetime.SequentialLifetimes
import com.jetbrains.rider.godot.community.actions.StartGodotEditorAction
import common.util.GdScriptProjectLifetimeService
import gdscript.GdScriptBundle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Service(Service.Level.PROJECT)
class GodotLspNotification(val project: Project, private val coroutineScope: CoroutineScope) {

    companion object {
        private const val GROUP_ID = "GodotSupportNotificationGroupId"
        
        fun getService(project: Project): GodotLspNotification = project.service<GodotLspNotification>()
    }

    private val pluginLifetime = GdScriptProjectLifetimeService.getLifetime(project)
    private val nestedLifetimeDef = pluginLifetime.createNested()
    private val sequentialLifetimes = SequentialLifetimes(nestedLifetimeDef)

    /**
     * Shows a warning notification when LSP attempts to connect to a non-matching project
     */
    fun showNonMatchingProjectWarning() {
        val title = GdScriptBundle.message("notification.title.godot.lsp.warning")
        val content = GdScriptBundle.message("notification.content.lsp.attempted.to.connect.to.non.matching.project")
        val notificationLifetime = sequentialLifetimes.next()

        val notification = Notification(
            GROUP_ID,
            title,
            content,
            NotificationType.WARNING
        )

        notification.addAction(object : NotificationAction(
            @Suppress("DialogTitleCapitalization")
            GdScriptBundle.message("action.StartEditorAction.text")) {
            override fun actionPerformed(e: AnActionEvent, notification: Notification) {
                StartGodotEditorAction.startEditor(project)
                notificationLifetime.terminate()
            }
        })

        val job = coroutineScope.launch(Dispatchers.EDT) {
            Notifications.Bus.notify(notification, project)
            notificationLifetime.onTermination { notification.expire() }
        }
        notificationLifetime.onTermination { job.cancel() }
    }
}
