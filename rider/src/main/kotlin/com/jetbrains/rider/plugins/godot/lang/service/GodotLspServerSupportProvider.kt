package com.jetbrains.rider.plugins.godot.lang.service

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.openapi.rd.util.lifetime
import com.intellij.openapi.rd.util.startNonUrgentBackgroundAsync
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.lsp.api.LspCommunicationChannel
import com.intellij.platform.lsp.api.LspServerManager
import com.intellij.platform.lsp.api.LspServerSupportProvider
import com.intellij.platform.lsp.api.ProjectWideLspServerDescriptor
import com.jetbrains.rd.util.reactive.adviseNotNull
import com.jetbrains.rd.util.threading.coroutines.noAwait
import com.jetbrains.rider.model.godot.frontendBackend.LanguageServerConnectionMode
import com.jetbrains.rider.plugins.godot.GodotProjectDiscoverer
import kotlinx.coroutines.delay
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

class GodotLspServerSupportProvider: LspServerSupportProvider {

    override fun fileOpened(project: Project, file: VirtualFile, serverStarter: LspServerSupportProvider.LspServerStarter) {
        val discoverer = GodotProjectDiscoverer.getInstance(project)
        discoverer.lspConnectionMode.adviseNotNull(project.lifetime) { lspConnectionMode ->
            if (lspConnectionMode == LanguageServerConnectionMode.Never) return@adviseNotNull
            discoverer.remoteHostPort.adviseNotNull(project.lifetime) { remoteHostPort ->
                discoverer.godotDescriptor.adviseNotNull(project.lifetime) {
                    if (Util.isSupportedFile(file)) {
                        discoverer.godotPath.adviseNotNull(project.lifetime) {
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
//                            } else {
                                thisLogger().info("ensureServerStarted")
                                serverStarter.ensureServerStarted(GodotLspServerDescriptor(project)) // this does not start the server, if executed after fileOpened already ended
                                LspServerManager.getInstance(project).startServersIfNeeded(this.javaClass)
//                            }
                        }
                    }
                }
            }
        }
    }

    private fun pingSocket(port: Int): Boolean {
        return try {
            thisLogger().info("pingSocket1")
            Socket().use { socket ->
                thisLogger().info("pingSocket2")
                socket.connect(InetSocketAddress("127.0.0.1", port)) // set connection timeout to 1000ms
                true
            }
        } catch (e: IOException) {
            false
        }
    }

    private class GodotLspServerDescriptor(project: Project) : ProjectWideLspServerDescriptor(project, "Godot") {

        val discoverer = GodotProjectDiscoverer.getInstance(project)
        val lspConnectionMode by lazy { discoverer.lspConnectionMode.value }
        val remoteHostPort by lazy { discoverer.remoteHostPort.value }

        override fun isSupportedFile(file: VirtualFile) = Util.isSupportedFile(file)
        override fun createCommandLine(): GeneralCommandLine {
            val basePath = discoverer.godotDescriptor.value?.mainProjectBasePath
            val godotPath = discoverer.godotPath.value
            val headlessArg = if (discoverer.godot4Path.value != null) "--headless" else "--no-window"
            val commandLine = GeneralCommandLine(godotPath, "--path", basePath, "--editor", headlessArg, "--lsp-port=$remoteHostPort")
            thisLogger().info("createCommandLine commandLine=$commandLine")
            return commandLine
            // https://github.com/godotengine/godot-proposals/issues/7558#issuecomment-1693765359
        }

        override val lspCommunicationChannel: LspCommunicationChannel
            get() {
                thisLogger().info("lspCommunicationChannel port=$remoteHostPort, mode=$lspConnectionMode")
                return LspCommunicationChannel.Socket(remoteHostPort!!,
                    lspConnectionMode == LanguageServerConnectionMode.StartEditorHeadless)
            }
    }

    object Util {
        fun isSupportedFile(file: VirtualFile) = file.extension?.equals("gd", true)?:false
    }
}
