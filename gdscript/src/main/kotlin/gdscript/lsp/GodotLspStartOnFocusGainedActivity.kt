package gdscript.lsp

import com.intellij.openapi.application.ApplicationActivationListener
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.intellij.openapi.wm.IdeFrame
import com.jetbrains.rider.godot.community.utils.GodotCommunityUtil
import com.jetbrains.rider.godot.community.utils.GodotFileUtil
import common.util.GdScriptProjectLifetimeService
import gdscript.settings.GdLspConnectionMode
import gdscript.settings.GdLspSettingsFlowService
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

private val RETRY_DELAYS_MS: List<Duration> = arrayOf(1000L, 2000L, 4000L).map { it.milliseconds } // 7s in total

/**
 * Ensures LSP runs for Godot projects on IDE focus with open GD files
 */
class GodotLspStartOnFocusGainedActivity : ProjectActivity {
    override suspend fun execute(project: Project) {
        // Await for godot
        GodotCommunityUtil.awaitGodotProject(project)

        val scope = GdScriptProjectLifetimeService.getScope(project)
        var retryJob: Job? = null

        ApplicationManager.getApplication().messageBus.connect(
            GdScriptProjectLifetimeService.getInstance(project)
        ).subscribe(
            ApplicationActivationListener.TOPIC,
            object : ApplicationActivationListener {
                override fun applicationActivated(ideFrame: IdeFrame) {
                    // Return early if the project is no longer a Godot one
                    if (!GodotCommunityUtil.isGodotProject(project)) return
                    val mode = GdLspSettingsFlowService.getInstance(project).lspConnectionMode.value
                    if (mode == null || mode == GdLspConnectionMode.Never) return
                    val hasGdFile = FileEditorManager.getInstance(project).openFiles.any { GodotFileUtil.isGdFile(it) }
                    if (!hasGdFile) return

                    retryJob?.cancel()
                    retryJob = scope.launch {
                        GodotLspRunningStatusProvider.ensureLspRunning(project)
                        for (delayMs in RETRY_DELAYS_MS) {
                            delay(delayMs)
                            GodotLspRunningStatusProvider.ensureLspRunning(project)
                        }
                    }
                }
            }
        )
    }
}
