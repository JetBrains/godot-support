@file:Suppress("UnstableApiUsage")

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
import com.intellij.platform.lsp.api.LspClient
import com.intellij.platform.lsp.api.LspClientManager
import com.intellij.platform.lsp.api.LspClientManagerListener
import com.intellij.platform.lsp.api.LspServerState
import com.jetbrains.rd.util.lifetime.SequentialLifetimes
import com.jetbrains.rider.godot.community.GdScriptProjectLifetimeService
import com.jetbrains.rider.godot.community.actions.StartGodotEditorAction
import gdscript.GdScriptBundle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Service(Service.Level.PROJECT)
class GodotLspNotification(val project: Project, private val coroutineScope: CoroutineScope) {

    companion object {
        private const val GROUP_ID = "GodotSupportNotificationGroupId"

        fun getService(project: Project): GodotLspNotification = project.service()
    }

    /**
     * Tracks the lifetime of the currently visible "non-matching project" warning notification.
     */
    private val sessionLifetimes = SequentialLifetimes(GdScriptProjectLifetimeService.getLifetime(project))

    init {
        LspClientManager.getInstance(project).addListener(
            object : LspClientManagerListener {
                override fun serverStateChanged(lspClient: LspClient) {
                    if (lspClient.providerClass != GodotLspIntegrationProvider::class.java) return
                    if (lspClient.state == LspServerState.Running) {
                        // Drop any non-matching-project warning still shown from the previous session.
                        sessionLifetimes.terminateCurrent()
                    }
                }
            },
            GdScriptProjectLifetimeService.getInstance(project)
        )
    }

    /**
     * Shows a warning notification when LSP attempts to connect to a non-matching project.
     */
    fun showNonMatchingProjectWarning() {
        val title = GdScriptBundle.message("notification.title.godot.lsp.warning")
        val content = GdScriptBundle.message("notification.content.lsp.attempted.to.connect.to.non.matching.project")

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
            }
        })

        val notificationLifetime = sessionLifetimes.next().lifetime
        coroutineScope.launch(Dispatchers.EDT) {
            Notifications.Bus.notify(notification, project)
            notificationLifetime.onTermination { notification.expire() }
        }
    }
}
