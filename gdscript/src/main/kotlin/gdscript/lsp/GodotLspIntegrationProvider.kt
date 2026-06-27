@file:Suppress("UnstableApiUsage")

package gdscript.lsp

import GdScriptPluginIcons
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.lsp.api.Lsp4jClient
import com.intellij.platform.lsp.api.LspCommunicationChannel
import com.intellij.platform.lsp.api.LspClient
import com.intellij.platform.lsp.api.LspClientDescriptor
import com.intellij.platform.lsp.api.LspClientManager
import com.intellij.platform.lsp.api.LspServerNotificationsHandler
import com.intellij.platform.lsp.api.LspIntegrationProvider
import com.intellij.platform.lsp.api.customization.LspCompletionCustomizer
import com.intellij.platform.lsp.api.customization.LspCustomization
import com.intellij.platform.lsp.api.customization.LspDiagnosticsCustomizer
import com.intellij.platform.lsp.api.customization.LspDiagnosticsSupport
import com.intellij.platform.lsp.api.customization.LspHoverCustomizer
import com.intellij.platform.lsp.api.customization.LspHoverDisabled
import com.intellij.platform.lsp.api.customization.LspInlayHintCustomizer
import com.intellij.platform.lsp.api.customization.LspInlayHintDisabled
import com.intellij.platform.lsp.api.lsWidget.LspClientWidgetItem
import com.intellij.util.NetworkUtils
import com.intellij.util.ui.update.DebouncedUpdates
import com.intellij.util.ui.update.UpdateQueue
import com.jetbrains.rider.godot.community.GdProjectGodotService
import com.jetbrains.rider.godot.community.GodotMajorVersion
import com.jetbrains.rider.godot.community.utils.GodotCommunityUtil
import com.jetbrains.rider.godot.community.utils.GodotFileUtil
import gdscript.settings.GdDocProviderMode
import gdscript.settings.GdLspConnectionMode
import gdscript.settings.GdLspSettingsFlowService
import gdscript.settings.GdProjectSettingsState
import gdscript.settings.GdSettingsConfigurable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import org.eclipse.lsp4j.ClientCapabilities
import org.eclipse.lsp4j.CompletionCapabilities
import org.eclipse.lsp4j.CompletionItemCapabilities
import org.eclipse.lsp4j.Diagnostic
import org.eclipse.lsp4j.MarkdownCapabilities
import org.eclipse.lsp4j.TextDocumentClientCapabilities
import kotlin.time.Duration.Companion.seconds

@Service(Service.Level.PROJECT)
class GodotLspProjectService(val project: Project, scope: CoroutineScope) {

    companion object {
        fun getInstance(project: Project): GodotLspProjectService = project.service<GodotLspProjectService>()
    }

    private val restartQueue: UpdateQueue<Unit> =
        DebouncedUpdates.forScope<Unit>(scope, "GodotLspRestart", 1.seconds)
            .restartTimerOnAdd(true)
            .runLatest { rebuildAndStart() }

    init {
        val settings = GdLspSettingsFlowService.getInstance(project)

        scope.launch(Dispatchers.IO) {
            settings.lspConnectionMode.filterNotNull().collect { mode ->
                if (mode == GdLspConnectionMode.Never) {
                    LspClientManager.getInstance(project).stopClients(GodotLspIntegrationProvider::class.java)
                } else {
                    restartQueue.queue(Unit)
                }
            }
        }

        scope.launch(Dispatchers.IO) {
            settings.remoteHostPort.filterNotNull().collect { restartQueue.queue(Unit) }
        }

        scope.launch(Dispatchers.IO) {
            GodotCommunityUtil.getGodotProjectBasePathFlow(project)
                .filterNotNull().collect { restartQueue.queue(Unit) }
        }

        // Initial rebuild — debounced together with whatever the flows above emit on first collect.
        restartQueue.queue(Unit)
    }

    fun queueRestart() {
        restartQueue.queue(Unit)
    }

    private suspend fun rebuildAndStart() {
        val settings = GdLspSettingsFlowService.getInstance(project)
        val manager = LspClientManager.getInstance(project)
        if (!allReady(settings, project)) {
            manager.stopClients(GodotLspIntegrationProvider::class.java)
            return
        }

        val basePath = GodotCommunityUtil.getGodotProjectBasePath(project)
        val port = basePath?.let { RunningGodotEditorDiscovery.findRunningGodotLspPort(it) }
        if (port != null) {
            thisLogger().info("Reusing --lsp-port=$port from a running Godot editor for $basePath")
        }

        thisLogger().info("rebuildAndStart: stop + ensureClientStarted")
        manager.stopClients(GodotLspIntegrationProvider::class.java)
        manager.ensureClientStarted(
            GodotLspIntegrationProvider::class.java,
            GodotLspClientDescriptor(project, port),
        )
    }

    private fun allReady(settings: GdLspSettingsFlowService, project: Project): Boolean = (
        settings.lspConnectionMode.value != GdLspConnectionMode.Never
            && (settings.lspConnectionMode.value == GdLspConnectionMode.ConnectRunningEditor || GodotCommunityUtil.getGodotExecutablePath(
            project
        ) != null)
            && settings.remoteHostPort.value != null
            && GodotCommunityUtil.getGodotProjectBasePath(project) != null
        )
}

class GodotLspIntegrationProvider : LspIntegrationProvider {
    override fun createWidgetItem(lspClient: LspClient, currentFile: VirtualFile?): LspClientWidgetItem =
        GodotLspClientWidgetItem(
            lspClient,
            currentFile,
            GdScriptPluginIcons.Icons.GodotLogo,
            settingsPageClass = GdSettingsConfigurable::class.java
        )

    override fun fileOpened(
        project: Project,
        file: VirtualFile,
        clientStarter: LspIntegrationProvider.LspClientStarter
    ) {
        if (!GodotFileUtil.isGdFile(file)) return
        GodotLspProjectService.getInstance(project)
    }
}

private class GodotLspClientDescriptor(
    project: Project,
    private val discoveredRunningLspPort: Int?,
) : LspClientDescriptor(
    project,
    "Godot",
    GodotCommunityUtil.getGodotProjectBasePath(project)?.let { VfsUtil.findFile(it, false) }!!
) {
    val settings = GdLspSettingsFlowService.getInstance(project)
    val lspConnectionMode by lazy { settings.lspConnectionMode.value }

    val remoteHostPort: Int? by lazy {
        if (useDynamicPort) NetworkUtils.findFreePort(500050) else
            discoveredRunningLspPort ?: settings.remoteHostPort.value
    }

    //val dapPort by lazy { NetworkUtils.findFreePort(500060, setOf()) }
    val useDynamicPort by lazy { settings.lspConnectionMode.value == GdLspConnectionMode.StartEditorHeadless }

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
        // todo: consider adding dap-port here too? However not sure about it.
        thisLogger().info("createCommandLine commandLine=$commandLine")
        return commandLine
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

    override val clientCapabilities: ClientCapabilities
        get() = super.clientCapabilities.apply {
            // RIDER-129495 Allows converting [color] BBCode to <span>
            general?.markdown = MarkdownCapabilities().apply {
                parser = "marked"
                version = "1.1.0"
                allowedTags = listOf("span")
            }
            // RIDER-134419 choosing function from autocomplete options leads to unwanted parentheses
            // Godot's LSP snippetSupport is for functions with parameters, we have a client handler for the case
            textDocument = (textDocument ?: TextDocumentClientCapabilities()).apply {
                completion = (completion ?: CompletionCapabilities()).apply {
                    completionItem = (completionItem ?: CompletionItemCapabilities()).apply {
                        snippetSupport = false
                    }
                }
            }
        }

    override fun createLsp4jClient(handler: LspServerNotificationsHandler): Lsp4jClient {
        return GodotLsp4jClient(handler, project)
    }

    override val lspCustomization: LspCustomization = object : LspCustomization() {
        override val completionCustomizer: LspCompletionCustomizer = GodotLspCompletionSupport()
        override val hoverCustomizer: LspHoverCustomizer
            get() {
                if (GdProjectSettingsState.getInstance(project).state.docProvider != GdDocProviderMode.LSP)
                    return LspHoverDisabled
                return super.hoverCustomizer
            }

        override val inlayHintCustomizer: LspInlayHintCustomizer = LspInlayHintDisabled

        override val diagnosticsCustomizer: LspDiagnosticsCustomizer = object : LspDiagnosticsSupport() {
            override fun getHighlightSeverity(diagnostic: Diagnostic): HighlightSeverity? {
                // RIDER-117554, also fixed in Godot 4.7 https://github.com/godotengine/godot/pull/114185
                val beforeGodot47 =
                    GdProjectGodotService.getInstance(project).projectInfoFlow.value?.parsedVersion?.lessThan(4, 7)
                        ?: false
                if (diagnostic.message.startsWith("(UNUSED_PARAMETER)") && beforeGodot47) {
                    return null
                }

                return super.getHighlightSeverity(diagnostic)
            }

            /*
            * UNUSED_VARIABLE, UNUSED_LOCAL_CONSTANT, UNUSED_PRIVATE_CLASS_VARIABLE, UNUSED_PARAMETER, UNUSED_SIGNAL
            * https://github.com/godotengine/godot/blob/1bd7b99182f7e8de4d6b2f089fec5db9392ac6b8/modules/gdscript/gdscript_warning.cpp#L47C8-L47C23
            */
            override fun getSpecialHighlightType(diagnostic: Diagnostic): ProblemHighlightType? {
                if (diagnostic.message.startsWith("(UNUSED_VARIABLE)")
                    || diagnostic.message.startsWith("(UNUSED_LOCAL_CONSTANT)")
                    || diagnostic.message.startsWith("(UNUSED_PRIVATE_CLASS_VARIABLE)")
                    || diagnostic.message.startsWith("(UNUSED_PARAMETER)")
                    || diagnostic.message.startsWith("(UNUSED_SIGNAL)")
                ) {
                    return ProblemHighlightType.LIKE_UNUSED_SYMBOL
                }
                return super.getSpecialHighlightType(diagnostic)
            }
        }
    }
}
