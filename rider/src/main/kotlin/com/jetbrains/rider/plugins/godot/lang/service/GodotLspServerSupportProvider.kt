package com.jetbrains.rider.plugins.godot.lang.service

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.lsp.api.*
import com.intellij.platform.lsp.api.customization.LspCompletionCustomizer
import com.intellij.platform.lsp.api.customization.LspCompletionSupport
import com.intellij.platform.lsp.api.customization.LspCustomization
import com.intellij.platform.lsp.api.customization.LspHoverCustomizer
import com.intellij.platform.lsp.api.customization.LspHoverDisabled
import com.intellij.platform.lsp.api.customization.LspInlayHintCustomizer
import com.intellij.platform.lsp.api.customization.LspInlayHintDisabled
import com.intellij.platform.lsp.api.customization.LspDiagnosticsCustomizer
import com.intellij.platform.lsp.api.customization.LspDiagnosticsSupport
import com.intellij.platform.lsp.api.lsWidget.LspServerWidgetItem
import com.intellij.util.NetworkUtils
import com.intellij.util.ui.update.MergingUpdateQueue
import com.intellij.util.ui.update.Update
import com.jetbrains.rd.util.reactive.adviseNotNull
import com.jetbrains.rd.util.reactive.hasValue
import com.jetbrains.rd.util.threading.coroutines.async
import com.jetbrains.rider.model.godot.frontendBackend.LanguageServerConnectionMode
import com.jetbrains.rider.plugins.godot.GodotIcons
import com.jetbrains.rider.plugins.godot.GodotProjectDiscoverer
import com.jetbrains.rider.plugins.godot.GodotProjectLifetimeService
import com.jetbrains.rider.plugins.godot.Util
import com.jetbrains.rider.plugins.godot.gdscript.PluginInterop
import com.jetbrains.rider.plugins.godot.settings.GodotPluginOptionsPage
import kotlinx.coroutines.Dispatchers
import java.util.concurrent.atomic.AtomicBoolean

@Service(Service.Level.PROJECT)
class GodotLspProjectService(val project: Project) {
    var isScheduled: AtomicBoolean = AtomicBoolean(false)

    companion object {
        fun getInstance(project: Project) = project.service<GodotLspProjectService>()
    }

    fun queueRestart(){
        mergingUpdateQueue.queue(mergingUpdateQueueAction)
    }

    private val mergingUpdateQueue: MergingUpdateQueue = MergingUpdateQueue("GodotLspServerSupportProviderMergingUpdateQueue", 1000, true, null).setRestartTimerOnAdd(true)
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
    override fun createLspServerWidgetItem(lspServer: LspServer, currentFile: VirtualFile?): LspServerWidgetItem = GodotLspServerWidgetItem(lspServer, currentFile, GodotIcons.Icons.GodotLogo, settingsPageClass = GodotPluginOptionsPage::class.java)

    override fun fileOpened(project: Project, file: VirtualFile, serverStarter: LspServerSupportProvider.LspServerStarter) {
        if (Util.isGdFile(file)) {
            val discoverer = GodotProjectDiscoverer.getInstance(project)
            val lspService = GodotLspProjectService.getInstance(project)

            // subscribe one time to everything
            if (lspService.isScheduled.compareAndSet(false, true)){
                val pluginLifetime = GodotProjectLifetimeService.getLifetime(project)
                pluginLifetime.async(Dispatchers.IO) {
                    discoverer.lspConnectionMode.adviseNotNull(pluginLifetime) { lspConnectionMode ->
                        if (lspConnectionMode == LanguageServerConnectionMode.Never) {
                            LspServerManager.getInstance(project).stopServers(GodotLspServerSupportProvider::class.java)
                        }
                        else
                            scheduleStartIfNeeded(project)
                    }

                    discoverer.useDynamicPort.adviseNotNull(pluginLifetime) {
                        scheduleStartIfNeeded(project)
                    }
                    discoverer.godotDescriptor.adviseNotNull(pluginLifetime) {
                        scheduleStartIfNeeded(project)
                    }
                    discoverer.godotPath.adviseNotNull(pluginLifetime) {
                        scheduleStartIfNeeded(project)
                    }

                    // RIDER-127016 Trigger Godot LSP reconnect with Editor start
                    discoverer.projectMetadataModificationSignal.adviseNotNull(pluginLifetime) {
                        if (discoverer.lspConnectionMode.value == LanguageServerConnectionMode.ConnectRunningEditor) {
                            scheduleStartIfNeeded(project)
                        }
                    }
                }
            }

            // start, it is ok to call multiple times, but not too often
            if (allReady(discoverer)) {
                thisLogger().info("ensureServerStarted")
                // this does not start a server if the `fileOpened` has already ended
                serverStarter.ensureServerStarted(GodotLspServerDescriptor(project))
            }
        }
    }

    private fun allReady(discoverer: GodotProjectDiscoverer) = (
        discoverer.lspConnectionMode.value != LanguageServerConnectionMode.Never
            && discoverer.godotPath.hasValue
            && discoverer.remoteHostPort.value != null
            && discoverer.godotDescriptor.valueOrNull != null)

    private fun scheduleStartIfNeeded(project: Project) {
        val godotLspProjectService = GodotLspProjectService.getInstance(project)
        val discoverer = GodotProjectDiscoverer.getInstance(project)
        if (!allReady(discoverer)) return
        godotLspProjectService.queueRestart()
    }

    private class GodotLspServerDescriptor(project: Project) : ProjectWideLspServerDescriptor(project, "Godot") {
        val discoverer = GodotProjectDiscoverer.getInstance(project)
        val lspConnectionMode by lazy { discoverer.lspConnectionMode.value }
        val remoteHostPort by lazy { if (useDynamicPort) NetworkUtils.findFreePort(500050) else discoverer.remoteHostPort.value }
        //val dapPort by lazy { NetworkUtils.findFreePort(500060, setOf()) }
        val useDynamicPort by lazy { discoverer.useDynamicPort.value!! && (discoverer.lspConnectionMode.value == LanguageServerConnectionMode.StartEditorHeadless) }

        override fun isSupportedFile(file: VirtualFile) = Util.isGdFile(file)
        override fun createCommandLine(): GeneralCommandLine {
            val basePath = discoverer.godotDescriptor.valueOrNull?.mainProjectBasePath
            val godotPath = discoverer.godotPath.valueOrNull
            val headlessArg = if (discoverer.godot4Path.value != null) "--headless" else "--no-window"
            // todo: dap port in the headless Godot may conflict with dap port of the main Godot editor
            val commandLine = GeneralCommandLine(godotPath, "--path", "$basePath", "--editor", headlessArg, "--lsp-port", remoteHostPort.toString())
            // can be solved with https://github.com/godotengine/godot/pull/92336, when it is released
            // val commandLine = GeneralCommandLine(godotPath, "--path", "$basePath", "--editor", headlessArg, "--lsp-port", remoteHostPort.toString(), "--dap-port", dapPort.toString())
            thisLogger().info("createCommandLine commandLine=$commandLine")
            return commandLine
            // https://github.com/godotengine/godot-proposals/issues/7558#issuecomment-1693765359
        }

        override val lspCommunicationChannel: LspCommunicationChannel
            get() {
                thisLogger().info("lspCommunicationChannel port=$remoteHostPort, mode=$lspConnectionMode")
                return LspCommunicationChannel.Socket(remoteHostPort!!, lspConnectionMode == LanguageServerConnectionMode.StartEditorHeadless)
            }

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
                    if (PluginInterop.isGdScriptPluginEnabled())
                        return LspHoverDisabled
                    return super.hoverCustomizer
                }

            override val inlayHintCustomizer: LspInlayHintCustomizer = LspInlayHintDisabled

            override val diagnosticsCustomizer: LspDiagnosticsCustomizer = object : LspDiagnosticsSupport() {
                override fun getHighlightSeverity(diagnostic: org.eclipse.lsp4j.Diagnostic): com.intellij.lang.annotation.HighlightSeverity? {
                    // RIDER-117554
                    if (diagnostic.message.startsWith("(UNUSED_PARAMETER)")) return null
                    return super.getHighlightSeverity(diagnostic)
                }
            }
        }
    }
}
