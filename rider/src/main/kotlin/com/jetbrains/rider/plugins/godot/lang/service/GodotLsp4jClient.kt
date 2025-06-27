package com.jetbrains.rider.plugins.godot.lang.service

import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.platform.lsp.api.Lsp4jClient
import com.intellij.platform.lsp.api.LspServerManager
import com.intellij.platform.lsp.api.LspServerNotificationsHandler
import com.jetbrains.rider.plugins.godot.GodotProjectDiscoverer
import org.eclipse.lsp4j.jsonrpc.services.JsonNotification
import kotlin.io.path.Path

/**
 * Custom Lsp4jClient implementation to catch the "gdscript_client/changeWorkspace" message.
 */
class GodotLsp4jClient(
    handler: LspServerNotificationsHandler,
    private val project: Project
) : Lsp4jClient(handler) {

    private class GodotChangeWorkspaceParams(
        val path: String
    )
    
    @JsonNotification("gdscript_client/changeWorkspace")
    private fun changeWorkspace(params: GodotChangeWorkspaceParams) {
        thisLogger().info("Received gdscript_client/changeWorkspace notification with path: ${params.path}")
        
        val discoverer = GodotProjectDiscoverer.getInstance(project)
        val godotBasePath = discoverer.godotDescriptor.valueOrNull?.mainProjectBasePath?.let { Path(it)}
        val workspacePath = Path(params.path)

        if (godotBasePath != workspacePath) {
            thisLogger().warn(
                "Workspace path $workspacePath doesn't match Godot base path $godotBasePath, disconnecting LSP server")

            GodotLspNotification.getService(project).showNonMatchingProjectWarning()
            LspServerManager.getInstance(project).stopServers(GodotLspServerSupportProvider::class.java)
        }
    }
}