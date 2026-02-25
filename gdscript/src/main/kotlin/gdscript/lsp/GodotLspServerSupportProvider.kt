package gdscript.lsp

import GdScriptPluginIcons
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.lsp.api.Lsp4jClient
import com.intellij.platform.lsp.api.LspCommunicationChannel
import com.intellij.platform.lsp.api.LspServer
import com.intellij.platform.lsp.api.LspServerManager
import com.intellij.platform.lsp.api.LspServerNotificationsHandler
import com.intellij.platform.lsp.api.LspServerSupportProvider
import com.intellij.platform.lsp.api.ProjectWideLspServerDescriptor
import com.intellij.platform.lsp.api.customization.LspCompletionCustomizer
import com.intellij.platform.lsp.api.customization.LspCompletionSupport
import com.intellij.platform.lsp.api.customization.LspCustomization
import com.intellij.platform.lsp.api.customization.LspDiagnosticsCustomizer
import com.intellij.platform.lsp.api.customization.LspDiagnosticsSupport
import com.intellij.platform.lsp.api.customization.LspHoverCustomizer
import com.intellij.platform.lsp.api.customization.LspHoverDisabled
import com.intellij.platform.lsp.api.customization.LspInlayHintCustomizer
import com.intellij.platform.lsp.api.customization.LspInlayHintDisabled
import com.intellij.platform.lsp.api.lsWidget.LspServerWidgetItem
import com.intellij.util.NetworkUtils
import com.intellij.util.ui.update.MergingUpdateQueue
import com.intellij.util.ui.update.Update
import com.jetbrains.rider.godot.community.GodotMajorVersion
import com.jetbrains.rider.godot.community.GodotMetadataService
import com.jetbrains.rider.godot.community.utils.GodotCommunityUtil
import com.jetbrains.rider.godot.community.utils.GodotFileUtil
import common.util.GdScriptProjectLifetimeService
import gdscript.settings.GdLspConnectionMode
import gdscript.settings.GdLspSettingsFlowService
import gdscript.settings.GdSettingsConfigurable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import org.eclipse.lsp4j.Diagnostic
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.getValue

@Service(Service.Level.PROJECT)
class GodotLspProjectService(val project: Project) {
    var isScheduled: AtomicBoolean = AtomicBoolean(false)

    companion object {
        fun getInstance(project: Project) = project.service<GodotLspProjectService>()
    }

    fun queueRestart() {
        mergingUpdateQueue.queue(mergingUpdateQueueAction)
    }

    private val mergingUpdateQueue: MergingUpdateQueue =
        MergingUpdateQueue("GodotLspServerSupportProviderMergingUpdateQueue", 1000, true, null).setRestartTimerOnAdd(true)
    private val mergingUpdateQueueAction: Update = object : Update("restartServerIfNeeded") {
        override fun run() {
            restartServer()
        }
    }

    fun restartServer() {
        thisLogger().info("stopAndRestartIfNeeded")
        LspServerManager.getInstance(project).stopAndRestartIfNeeded(GodotLspServerSupportProvider::class.java)
    }
}

class GodotLspServerSupportProvider : LspServerSupportProvider {
    override fun createLspServerWidgetItem(lspServer: LspServer, currentFile: VirtualFile?): LspServerWidgetItem =
        GodotLspServerWidgetItem(lspServer, currentFile, GdScriptPluginIcons.Icons.GodotLogo, settingsPageClass = GdSettingsConfigurable::class.java)

    override fun fileOpened(project: Project, file: VirtualFile, serverStarter: LspServerSupportProvider.LspServerStarter) {
        if (GodotFileUtil.isGdFile(file)) {
            val settings = GdLspSettingsFlowService.getInstance(project)
            val lspService = GodotLspProjectService.getInstance(project)

            // subscribe one time to everything
            if (lspService.isScheduled.compareAndSet(false, true)) {
                val scope = GdScriptProjectLifetimeService.getScope(project)
                scope.launch(Dispatchers.IO) {
                    settings.lspConnectionMode.filterNotNull().collect { lspConnectionMode ->
                        if (lspConnectionMode == GdLspConnectionMode.Never) {
                            LspServerManager.getInstance(project).stopServers(GodotLspServerSupportProvider::class.java)
                        } else
                            scheduleStartIfNeeded(project)
                    }
                }

                scope.launch(Dispatchers.IO) {
                    settings.useDynamicPort.filterNotNull().collect {
                        scheduleStartIfNeeded(project)
                    }
                }

                scope.launch(Dispatchers.IO) {
                    settings.remoteHostPort.filterNotNull().collect {
                        scheduleStartIfNeeded(project)
                    }
                }

                scope.launch(Dispatchers.IO) {
                    GodotCommunityUtil.isPureGdScriptProjectFlow(project).collect {
                        scheduleStartIfNeeded(project)
                    }
                }

                scope.launch(Dispatchers.IO) {
                    GodotCommunityUtil.getGodotProjectBasePathFlow(project).collect {
                        scheduleStartIfNeeded(project)
                    }
                }

                scope.launch(Dispatchers.IO) {
                    GodotMetadataService.getInstance(project).metadataChangeFlow
                        .filterNotNull()
                        .collect {
                        if (settings.lspConnectionMode.value == GdLspConnectionMode.ConnectRunningEditor) {
                            scheduleStartIfNeeded(project)
                        }
                    }
                }
            }

            // start, it is ok to call multiple times, but not too often
            if (allReady(settings, project)) {
                thisLogger().info("ensureServerStarted")
                // this does not start a server if the `fileOpened` has already ended
                serverStarter.ensureServerStarted(GodotLspServerDescriptor(project))
            }
        }
    }

    private fun allReady(discoverer: GdLspSettingsFlowService, project: Project) = (
        discoverer.lspConnectionMode.value != GdLspConnectionMode.Never
            && GodotCommunityUtil.getGodotExecutablePath(project) != null
            && discoverer.remoteHostPort.value != null
            && GodotCommunityUtil.getGodotProjectBasePath(project) != null
            && GodotCommunityUtil.isPureGdScriptProject(project) != null
        )

    private fun scheduleStartIfNeeded(project: Project) {
        val godotLspProjectService = GodotLspProjectService.getInstance(project)
        val discoverer = GdLspSettingsFlowService.getInstance(project)
        if (!allReady(discoverer, project)) return
        godotLspProjectService.queueRestart()
    }

    private class GodotLspServerDescriptor(project: Project) : ProjectWideLspServerDescriptor(project, "Godot") {
        val settings = GdLspSettingsFlowService.getInstance(project)
        val lspConnectionMode by lazy { settings.lspConnectionMode.value }
        val remoteHostPort by lazy { if (useDynamicPort) NetworkUtils.findFreePort(500050) else settings.remoteHostPort.value }

        //val dapPort by lazy { NetworkUtils.findFreePort(500060, setOf()) }
        val useDynamicPort by lazy { settings.useDynamicPort.value!! && (settings.lspConnectionMode.value == GdLspConnectionMode.StartEditorHeadless) }

        override fun isSupportedFile(file: VirtualFile) = GodotFileUtil.isGdFile(file)
        override fun createCommandLine(): GeneralCommandLine {
            val basePath = GodotCommunityUtil.getGodotProjectBasePath(project)
            val godotPath = GodotCommunityUtil.getGodotExecutablePath(project)
            val headlessArg = when (GodotCommunityUtil.getGodotMajorVersion(project)) {
                GodotMajorVersion.GODOT_4 -> "--headless"
                else -> "--no-window"
            }
            // todo: dap port in the headless Godot may conflict with dap port of the main Godot editor
            val commandLine = GeneralCommandLine(
                godotPath.toString(),
                "--path",
                "$basePath",
                "--editor",
                headlessArg,
                "--lsp-port",
                remoteHostPort.toString()
            )
            // can be solved with https://github.com/godotengine/godot/pull/92336, when it is released
            // val commandLine = GeneralCommandLine(godotPath, "--path", "$basePath", "--editor", headlessArg, "--lsp-port", remoteHostPort.toString(), "--dap-port", dapPort.toString())
            thisLogger().info("createCommandLine commandLine=$commandLine")
            return commandLine
            // https://github.com/godotengine/godot-proposals/issues/7558#issuecomment-1693765359
        }

        override val lspCommunicationChannel: LspCommunicationChannel
            get() {
                thisLogger().info("lspCommunicationChannel port=$remoteHostPort, mode=$lspConnectionMode")
                return LspCommunicationChannel.Socket(
                    remoteHostPort!!,
                    lspConnectionMode == GdLspConnectionMode.StartEditorHeadless
                )
            }

        override fun getLanguageId(file: VirtualFile) = "gdscript"

        override fun createLsp4jClient(handler: LspServerNotificationsHandler): Lsp4jClient {
            return GodotLsp4jClient(handler, project)
        }

        override val lspCustomization: LspCustomization = object : LspCustomization() {
            override val completionCustomizer: LspCompletionCustomizer = object : LspCompletionSupport() {
                override fun getCompletionPrefix(parameters: CompletionParameters, defaultPrefix: String): String =
                    // RIDER-119006 LSP Completion for GDScript doesn't work after "$"
                    if (defaultPrefix.startsWith("$")) defaultPrefix.substringAfter("$")
                    else defaultPrefix
            }
            override val hoverCustomizer: LspHoverCustomizer
                get() {
                    // GDScript plugin is always enabled, so we get hover from there
                    return LspHoverDisabled
                }

            override val inlayHintCustomizer: LspInlayHintCustomizer = LspInlayHintDisabled

            override val diagnosticsCustomizer: LspDiagnosticsCustomizer = object : LspDiagnosticsSupport() {
                override fun getHighlightSeverity(diagnostic: Diagnostic): HighlightSeverity? {
                    // RIDER-117554, also fixed in Godot 4.7 https://github.com/godotengine/godot/pull/114185
                    // todo: use Godot version here to only conditionally disable unused parameter highlighting for older Godot versions
                    if (diagnostic.message.startsWith("(UNUSED_PARAMETER)")) return null
                    return super.getHighlightSeverity(diagnostic)
                }
                /*
                UNUSED_VARIABLE, UNUSED_LOCAL_CONSTANT, UNUSED_PRIVATE_CLASS_VARIABLE, UNUSED_PARAMETER, UNUSED_SIGNAL
                https://github.com/godotengine/godot/blob/1bd7b99182f7e8de4d6b2f089fec5db9392ac6b8/modules/gdscript/gdscript_warning.cpp#L47C8-L47C23
                 */
                override fun getSpecialHighlightType(diagnostic: Diagnostic): ProblemHighlightType? {
                    if (diagnostic.message.startsWith("(UNUSED_VARIABLE)")
                        || diagnostic.message.startsWith("(UNUSED_LOCAL_CONSTANT)")
                        || diagnostic.message.startsWith("(UNUSED_PRIVATE_CLASS_VARIABLE)")
                        || diagnostic.message.startsWith("(UNUSED_PARAMETER)")
                        || diagnostic.message.startsWith("(UNUSED_SIGNAL)")) {
                        return ProblemHighlightType.LIKE_UNUSED_SYMBOL
                    }
                    return super.getSpecialHighlightType(diagnostic)
                }
            }
        }
    }
}
