package com.jetbrains.rider.plugins.godot.lang.service

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.PrioritizedLookupElement
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.SystemInfo
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.lsp.api.*
import com.intellij.platform.lsp.api.customization.LspCompletionSupport
import com.intellij.platform.lsp.api.lsWidget.LspServerWidgetItem
import com.intellij.util.ui.update.MergingUpdateQueue
import com.intellij.util.ui.update.Update
import com.jetbrains.rd.util.reactive.adviseNotNull
import com.jetbrains.rd.util.threading.coroutines.async
import com.jetbrains.rider.model.godot.frontendBackend.LanguageServerConnectionMode
import com.jetbrains.rider.plugins.godot.GodotIcons
import com.jetbrains.rider.plugins.godot.GodotProjectDiscoverer
import com.jetbrains.rider.plugins.godot.GodotProjectLifetimeService
import com.jetbrains.rider.plugins.godot.Util
import com.jetbrains.rider.plugins.godot.settings.GodotPluginOptionsPage
import com.jetbrains.rider.util.NetUtils
import kotlinx.coroutines.Dispatchers
import org.eclipse.lsp4j.CompletionItem
import java.nio.file.Path
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.io.path.exists
import kotlin.io.path.nameWithoutExtension
import kotlin.io.path.pathString

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
            && discoverer.godotPath.value != null
            && discoverer.remoteHostPort.value != null
            && discoverer.godotDescriptor.value != null)

    private fun scheduleStartIfNeeded(project: Project) {
        val godotLspProjectService = GodotLspProjectService.getInstance(project)
        val discoverer = GodotProjectDiscoverer.getInstance(project)
        if (!allReady(discoverer)) return
        godotLspProjectService.queueRestart()
    }

    private class GodotLspServerDescriptor(project: Project) : ProjectWideLspServerDescriptor(project, "Godot") {
        val discoverer = GodotProjectDiscoverer.getInstance(project)
        val lspConnectionMode by lazy { discoverer.lspConnectionMode.value }
        val remoteHostPort by lazy { if (useDynamicPort) NetUtils.findFreePort(500050, setOf()) else discoverer.remoteHostPort.value }
        //val dapPort by lazy { NetUtils.findFreePort(500060, setOf()) }
        val useDynamicPort by lazy { discoverer.useDynamicPort.value!! && (discoverer.lspConnectionMode.value == LanguageServerConnectionMode.StartEditorHeadless) }

        override fun isSupportedFile(file: VirtualFile) = Util.isGdFile(file)
        override fun createCommandLine(): GeneralCommandLine {
            val basePath = discoverer.godotDescriptor.value?.mainProjectBasePath
            var godotPath = discoverer.godotPath.value
            if (godotPath == null) throw Exception("godotPath is null")
            // https://github.com/godotengine/godot-proposals/issues/7558#issuecomment-1693765359
            val headlessArg = if (discoverer.godot4Path.value != null) "--headless" else "--no-window"
            // todo: dap port in the headless Godot may conflict with dap port of the main Godot editor
            if (SystemInfo.isWindows){
                // need to ensure that we start _console.exe, if it exists, otherwise LSP may not connect
                // "Godot_v4.4-stable_mono_win64_console.exe"
                val path = Path.of(godotPath)
                val possiblePath = path.parent.resolve("${path.nameWithoutExtension}_console.exe")
                if (possiblePath.exists())
                    godotPath = possiblePath.pathString
            }
            val commandLine = GeneralCommandLine(godotPath, "--path", "$basePath", "--editor", headlessArg, "--lsp-port", remoteHostPort.toString())
            // todo: use dynamic DAP port, which exists since Godot 4.3 https://github.com/godotengine/godot/pull/92336
            // val commandLine = GeneralCommandLine(godotPath, "--path", "$basePath", "--editor", headlessArg, "--lsp-port", remoteHostPort.toString(), "--dap-port", dapPort.toString())
            thisLogger().info("createCommandLine commandLine=$commandLine")
            return commandLine
        }

        override val lspCommunicationChannel: LspCommunicationChannel
            get() {
                thisLogger().info("lspCommunicationChannel port=$remoteHostPort, mode=$lspConnectionMode")
                return LspCommunicationChannel.Socket(remoteHostPort!!, lspConnectionMode == LanguageServerConnectionMode.StartEditorHeadless)
            }

        override val lspCompletionSupport: LspCompletionSupport
            get() = object : LspCompletionSupport() {
                override fun createLookupElement(parameters: CompletionParameters, item: CompletionItem): LookupElement? {
                    val item1 = super.createLookupElement(parameters, item) ?: return null
                    // we want to be more preferable than TextMate
                    return PrioritizedLookupElement.withPriority(item1, 1.0)
                }
             }
    }
}