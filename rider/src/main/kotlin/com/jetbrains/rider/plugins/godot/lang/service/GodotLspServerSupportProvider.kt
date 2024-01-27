package com.jetbrains.rider.plugins.godot.lang.service

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.openapi.rd.util.lifetime
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.lsp.api.LspCommunicationChannel
import com.intellij.platform.lsp.api.LspServerManager
import com.intellij.platform.lsp.api.LspServerSupportProvider
import com.intellij.platform.lsp.api.ProjectWideLspServerDescriptor
import com.intellij.util.ui.update.MergingUpdateQueue
import com.intellij.util.ui.update.Update
import com.jetbrains.rd.platform.util.idea.LifetimedService
import com.jetbrains.rd.util.lifetime.SequentialLifetimes
import com.jetbrains.rd.util.lifetime.isAlive
import com.jetbrains.rd.util.reactive.adviseNotNull
import com.jetbrains.rider.model.godot.frontendBackend.LanguageServerConnectionMode
import com.jetbrains.rider.plugins.godot.Util
import com.jetbrains.rider.plugins.godot.GodotProjectDiscoverer
import com.jetbrains.rider.util.NetUtils
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import java.util.concurrent.atomic.AtomicBoolean

@Service(Service.Level.PROJECT)
class LspProjectService(val project: Project) {
    var isScheduled: AtomicBoolean = AtomicBoolean(false)

    companion object {
        fun getInstance(project: Project) = project.service<LspProjectService>()
    }

    val sequentialLifetimes = SequentialLifetimes(project.lifetime)
    val mergingUpdateQueue: MergingUpdateQueue = MergingUpdateQueue("GodotLspServerSupportProviderMergingUpdateQueue", 1000, true, null).setRestartTimerOnAdd(true)
    val mergingUpdateQueueAction: Update = object : Update("restartServerIfNeeded") {
        override fun run() {
            restartServer(project)
        }
    }

    private fun restartServer(project: Project) {
        LspServerManager.getInstance(project).stopServers(GodotLspServerSupportProvider::class.java)
        thisLogger().info("startServersIfNeeded")
        LspServerManager.getInstance(project).startServersIfNeeded(GodotLspServerSupportProvider::class.java)
    }

    private fun pingSocket(port: Int): Boolean {
        return try {
            thisLogger().info("pingSocket1")
            Socket().use { socket ->
                thisLogger().info("pingSocket2")
                socket.connect(InetSocketAddress("127.0.0.1", port))
                true
            }
        } catch (e: IOException) {
            false
        }
    }
}

class GodotLspServerSupportProvider : LspServerSupportProvider {

    override fun fileOpened(project: Project, file: VirtualFile, serverStarter: LspServerSupportProvider.LspServerStarter) {
        if (Util.isGdFile(file)) {
            val discoverer = GodotProjectDiscoverer.getInstance(project)
            val lspService = LspProjectService.getInstance(project)
            if (allReady(discoverer)) {
                thisLogger().info("ensureServerStarted")
                serverStarter.ensureServerStarted(GodotLspServerDescriptor(project)) // this does not start the server, if fileOpened already ended
            } else if (lspService.isScheduled.compareAndSet(false, true)) {
                val lifetime = lspService.sequentialLifetimes.next()
                project.lifetime.onTerminationIfAlive { if (lifetime.isAlive) lifetime.terminate() }
                discoverer.lspConnectionMode.adviseNotNull(lifetime) { lspConnectionMode ->
                    if (lspConnectionMode == LanguageServerConnectionMode.Never) {
                        LspServerManager.getInstance(project).stopServers(this.javaClass)
                        return@adviseNotNull
                    }

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

// todo: restore
//                            if (lspConnectionMode == LanguageServerConnectionMode.ConnectRunningEditor) {
//                                thisLogger().info("fileOpened6 ${remoteHostPort}")
//                                project.lifetime.startNonUrgentBackgroundAsync {
//                                    thisLogger().info("fileOpened7 ${remoteHostPort}")
//                                    while (!pingSocket(remoteHostPort)) {
//                                        delay(1000)
//                                        thisLogger().info("fileOpened - delay")
//                                    }
//                                    thisLogger().info("ensureServerStarted1")
//                                    delay(500)
//                                    serverStarter.ensureServerStarted(GodotLspServerDescriptor(project))
//                                }.noAwait()
//                            }
            }
        }
    }

    private fun allReady(discoverer: GodotProjectDiscoverer) = (
        discoverer.lspConnectionMode.value != LanguageServerConnectionMode.Never
            && discoverer.godotPath.value != null
            && discoverer.remoteHostPort.value != null
            && discoverer.godotDescriptor.value != null)

    private fun scheduleStartIfNeeded(project: Project) {
        val lspProjectService = LspProjectService.getInstance(project)
        val discoverer = GodotProjectDiscoverer.getInstance(project)
        if (!allReady(discoverer)) return
        lspProjectService.mergingUpdateQueue.queue(lspProjectService.mergingUpdateQueueAction)
    }

    private class GodotLspServerDescriptor(project: Project) : ProjectWideLspServerDescriptor(project, "Godot") {
        val discoverer = GodotProjectDiscoverer.getInstance(project)
        val lspConnectionMode by lazy { discoverer.lspConnectionMode.value }
        val remoteHostPort by lazy { if (useDynamicPort!!) NetUtils.findFreePort(500050, setOf()) else discoverer.remoteHostPort.value }
        val useDynamicPort by lazy { discoverer.useDynamicPort.value }

        override fun isSupportedFile(file: VirtualFile) = Util.isGdFile(file)
        override fun createCommandLine(): GeneralCommandLine {
            val basePath = discoverer.godotDescriptor.value?.mainProjectBasePath
            val godotPath = discoverer.godotPath.value
            val headlessArg = if (discoverer.godot4Path.value != null) "--headless" else "--no-window"
            val commandLine = GeneralCommandLine(godotPath, "--path", "$basePath", "--editor", headlessArg, "--lsp-port", remoteHostPort.toString())
            thisLogger().info("createCommandLine commandLine=$commandLine")
            return commandLine
            // https://github.com/godotengine/godot-proposals/issues/7558#issuecomment-1693765359
        }

        override val lspCommunicationChannel: LspCommunicationChannel
            get() {
                thisLogger().info("lspCommunicationChannel port=$remoteHostPort, mode=$lspConnectionMode")
                return LspCommunicationChannel.Socket(remoteHostPort!!, lspConnectionMode == LanguageServerConnectionMode.StartEditorHeadless)
            }
    }
}
