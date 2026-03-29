package gdscript.lsp

import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.registry.Registry
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.lsp.api.LspServer
import com.intellij.platform.lsp.api.LspServerManager
import com.intellij.platform.lsp.api.LspServerManagerListener
import com.intellij.ui.EditorNotificationPanel
import com.intellij.ui.EditorNotificationProvider
import com.intellij.ui.EditorNotifications
import com.jetbrains.rider.godot.community.actions.StartGodotEditorAction
import com.jetbrains.rider.godot.community.utils.GodotCommunityUtil
import com.jetbrains.rider.godot.community.utils.GodotFileUtil
import com.jetbrains.rider.godot.community.utils.hasCompletedTrue
import common.util.GdScriptProjectLifetimeService
import gdscript.GdScriptBundle
import gdscript.settings.GdLspConnectionMode
import gdscript.settings.GdLspSettingsFlowService
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.atomic.AtomicReference
import java.util.function.Function
import javax.swing.JComponent

private const val IS_ENABLED_REGISTRY_KEY: String = "gdscript.lsp.not.connected.banner.enabled"

class GodotLspNotConnectedNotificationProvider(private val project: Project) : EditorNotificationProvider, DumbAware {

    companion object {
        private const val NOTIFICATION_DELAY_MS = 1000L
    }

    private val scope = GdScriptProjectLifetimeService.getScope(project)
    /** Timestamp (System.currentTimeMillis) when we first detected the "not connected" state, or 0 if LSP is connected. */
    private val disconnectedSince = AtomicLong(0L)
    private val pendingNotificationJob = AtomicReference<Job?>(null)

    init {
        // Update notifications on LSP server status change
        LspServerManager.getInstance(project).addLspServerManagerListener(
            object : LspServerManagerListener {
                override fun serverStateChanged(lspServer: LspServer) {
                    if (lspServer.providerClass == GodotLspServerSupportProvider::class.java) {
                        if (GodotLspRunningStatusProvider.isLspRunning(project)) {
                            // LSP connected — reset the timer
                            disconnectedSince.set(0L)
                        }
                        EditorNotifications.getInstance(project).updateAllNotifications()
                    }
                }
            },
            GdScriptProjectLifetimeService.getInstance(project)
        )
    }

    override fun collectNotificationData(
        project: Project,
        file: VirtualFile
    ): Function<in FileEditor, out JComponent?>? {
        if (!Registry.`is`(IS_ENABLED_REGISTRY_KEY, false)) return null
        if (!GodotFileUtil.isGdFile(file)) return null
        if (!GodotCommunityUtil.isGodotProject(project).hasCompletedTrue()) return null

        val settings = GdLspSettingsFlowService.getInstance(project)
        val lspConnectionMode = settings.lspConnectionMode.value

        if (lspConnectionMode == null || lspConnectionMode == GdLspConnectionMode.Never || lspConnectionMode == GdLspConnectionMode.StartEditorHeadless) {
            disconnectedSince.set(0L)
            return null
        }
        if (GodotLspRunningStatusProvider.isLspRunning(project)) return null
        if (GodotLspRunningStatusProvider.isLspStartingUp(project)) return null

        // Record when we first noticed the disconnected state
        val now = System.currentTimeMillis()
        disconnectedSince.compareAndSet(0L, now)
        val since = disconnectedSince.get()
        if (since == 0L) return null  // was reset between CAS and get
        val elapsed = now - disconnectedSince.get()

        if (elapsed < NOTIFICATION_DELAY_MS) {
            // Schedule a single re-check after the remaining delay (replaces any previous pending job)
            val newJob = scope.launch {
                delay(NOTIFICATION_DELAY_MS - elapsed)
                EditorNotifications.getInstance(project).updateAllNotifications()
            }
            pendingNotificationJob.getAndSet(newJob)?.cancel()
            return null
        }

        return Function { _: FileEditor ->
            EditorNotificationPanel(EditorNotificationPanel.Status.Warning).apply {
                toolTipText = GdScriptBundle.message("gdscript.lsp.notification.import.godot.project")
                text = GdScriptBundle.message("gdscript.lsp.notification.lsp.is.not.connected")

                @Suppress("DialogTitleCapitalization")
                val label = createActionLabel(GdScriptBundle.message("gdscript.lsp.notification.start.godot.editor.link")) {
                    StartGodotEditorAction.startEditor(project)
                }
                if (GodotCommunityUtil.getGodotExecutablePath(project) == null) {
                    label.isEnabled = false
                }
                createActionLabel(GdScriptBundle.message("gdscript.lsp.notification.disable.lsp.link")) {
                    disconnectedSince.set(0L)
                    GdLspSettingsFlowService.getInstance(project).setLspConnectionMode(GdLspConnectionMode.Never)
                    EditorNotifications.getInstance(project).updateAllNotifications()
                }
            }
        }
    }

}
