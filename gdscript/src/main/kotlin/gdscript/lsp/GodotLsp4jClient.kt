package gdscript.lsp

import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.platform.lsp.api.Lsp4jClient
import com.intellij.platform.lsp.api.LspClientManager
import com.intellij.platform.lsp.api.LspServerNotificationsHandler
import com.jetbrains.rider.godot.community.utils.GodotCommunityUtil
import org.eclipse.lsp4j.MessageParams
import org.eclipse.lsp4j.jsonrpc.services.JsonNotification
import kotlin.io.path.Path

/**
 * Custom Lsp4jClient implementation to:
 *  - catch the Godot-specific `gdscript_client/changeWorkspace` notification and disconnect on
 *    project mismatch;
 *  - suppress the standard `window/showMessage` notification, which Godot's GDScript LSP server
 *    emits only as a workspace-mismatch warning right next to `gdscript_client/changeWorkspace`
 *    (see `gdscript_language_protocol.cpp`). Showing it would just duplicate our own notification.
 */
class GodotLsp4jClient(
    handler: LspServerNotificationsHandler,
    private val project: Project
) : Lsp4jClient(SuppressShowMessageHandler(handler)) {

    private class GodotChangeWorkspaceParams(
        val path: String
    )

    @JsonNotification("gdscript_client/changeWorkspace")
    private fun changeWorkspace(params: GodotChangeWorkspaceParams) {
        thisLogger().info("Received gdscript_client/changeWorkspace notification with path: ${params.path}")

        val godotBasePath = GodotCommunityUtil.getGodotProjectBasePath(project)
        val workspacePath = Path(params.path)

        if (godotBasePath != workspacePath) {
            thisLogger().warn(
                "Workspace path $workspacePath doesn't match Godot base path $godotBasePath, disconnecting LSP server")

            GodotLspNotification.getService(project).showNonMatchingProjectWarning()
            LspClientManager.getInstance(project).stopClients(GodotLspIntegrationProvider::class.java)
        }
    }

    /**
     * Wraps the platform-provided [LspServerNotificationsHandler] and drops `window/showMessage`
     * notifications, which Godot uses to deliver the same workspace-mismatch warning that we
     * already handle via `gdscript_client/changeWorkspace`. All other notifications/requests are
     * forwarded unchanged.
     */
    private class SuppressShowMessageHandler(
        delegate: LspServerNotificationsHandler
    ) : LspServerNotificationsHandler by delegate {
        override fun showMessage(params: MessageParams) {
            thisLogger().info("Suppressed window/showMessage from Godot LSP: ${params.message}")
        }
    }
}