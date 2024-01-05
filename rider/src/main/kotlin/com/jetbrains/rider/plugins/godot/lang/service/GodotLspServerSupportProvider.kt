package com.jetbrains.rider.plugins.godot.lang.service

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.project.Project
import com.intellij.openapi.rd.util.lifetime
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.lsp.api.LspCommunicationChannel
import com.intellij.platform.lsp.api.LspServerSupportProvider
import com.intellij.platform.lsp.api.ProjectWideLspServerDescriptor
import com.jetbrains.rd.util.reactive.adviseNotNull
import com.jetbrains.rider.plugins.godot.GodotProjectDiscoverer

val port = 6005 // todo: depend on settings
class GodotLspServerSupportProvider: LspServerSupportProvider {

    override fun fileOpened(project: Project, file: VirtualFile, serverStarter: LspServerSupportProvider.LspServerStarter) {
        val discoverer = GodotProjectDiscoverer.getInstance(project)
        discoverer.godotDescriptor.adviseNotNull(project.lifetime) {
            if (Util.isSupportedFile(file)) {
                discoverer.godot4Path.adviseNotNull(project.lifetime) {
                    serverStarter.ensureServerStarted(GodotLspServerDescriptor(project))
                }
                discoverer.godot3Path.adviseNotNull(project.lifetime) {
                    serverStarter.ensureServerStarted(GodotLspServerDescriptor(project))
                }
            }
        }
    }

    private class GodotLspServerDescriptor(project: Project) : ProjectWideLspServerDescriptor(project, "Godot") {
        override fun isSupportedFile(file: VirtualFile) =
            Util.isSupportedFile(file)
        override fun createCommandLine(): GeneralCommandLine {
            val discoverer = GodotProjectDiscoverer.getInstance(project)
            val basePath = discoverer.godotDescriptor.value?.mainProjectBasePath
            val godotPath = discoverer.godot4Path.value ?: discoverer.godot3Path.value
            val headlessArg = if (discoverer.godot4Path.value != null) "--headless" else "--no-window"

            return GeneralCommandLine(godotPath, "--path", basePath, "--editor", headlessArg, "--lsp-port=$port")
            // https://github.com/godotengine/godot-proposals/issues/7558#issuecomment-1693765359
        }

        override val lspCommunicationChannel: LspCommunicationChannel
            get() {
                return LspCommunicationChannel.Socket(port, true)
            }
    }

    object Util {
        val extensions = setOf("gd")
        fun isSupportedFile(file: VirtualFile) = file.extension?.lowercase() in extensions
    }
}
