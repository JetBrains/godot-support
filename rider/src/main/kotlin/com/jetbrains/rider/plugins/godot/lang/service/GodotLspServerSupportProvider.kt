package com.jetbrains.rider.plugins.godot.lang.service

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.PrioritizedLookupElement
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.lsp.api.*
import com.intellij.platform.lsp.api.customization.LspFindReferencesSupport
import com.intellij.platform.lsp.api.customization.LspCompletionSupport
import com.intellij.platform.lsp.api.lsWidget.LspServerWidgetItem
import com.intellij.util.ui.update.MergingUpdateQueue
import com.intellij.util.ui.update.Update
import com.jetbrains.rd.util.reactive.adviseNotNull
import com.jetbrains.rider.model.godot.frontendBackend.LanguageServerConnectionMode
import com.jetbrains.rider.plugins.godot.GodotIcons
import com.jetbrains.rider.plugins.godot.GodotProjectDiscoverer
import com.jetbrains.rider.plugins.godot.GodotProjectLifetimeService
import com.jetbrains.rider.plugins.godot.Util
import com.jetbrains.rider.plugins.godot.gdscript.PluginInterop
import com.jetbrains.rider.plugins.godot.settings.GodotPluginOptionsPage
import com.jetbrains.rider.util.NetUtils
import org.eclipse.lsp4j.CompletionItem
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
                val lifetime = GodotProjectLifetimeService.getLifetime(project)
                discoverer.lspConnectionMode.adviseNotNull(lifetime) { lspConnectionMode ->
                    if (lspConnectionMode == LanguageServerConnectionMode.Never) {
                        LspServerManager.getInstance(project).stopServers(this.javaClass)
                    }
                    else
                        scheduleStartIfNeeded(project)
                }

                discoverer.useDynamicPort.adviseNotNull(lifetime) {
                    scheduleStartIfNeeded(project)
                }
                discoverer.godotDescriptor.adviseNotNull(lifetime) {
                    scheduleStartIfNeeded(project)
                }
                discoverer.godotPath.adviseNotNull(lifetime) {
                    scheduleStartIfNeeded(project)
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
            val godotPath = discoverer.godotPath.value
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

        override val lspCompletionSupport: LspCompletionSupport
            get() = object : LspCompletionSupport() {
                override fun createLookupElement(parameters: CompletionParameters, item: CompletionItem): LookupElement? {
                    val item1 = super.createLookupElement(parameters, item) ?: return null
                    // we want to be more preferable than TextMate
                    return PrioritizedLookupElement.withPriority(item1, 1.0)
                }
             }

        override val lspFindReferencesSupport: LspFindReferencesSupport?
            get() {
                if (PluginInterop.isGdScriptPluginEnabled())
                    return null
                return super.lspFindReferencesSupport
            }
    }
}
