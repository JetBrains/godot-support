package gdscript.lsp

import com.intellij.openapi.project.Project
import com.intellij.platform.lsp.api.LspClientManager
import com.intellij.platform.lsp.api.LspServerState

object GodotLspRunningStatusProvider {
    fun isLspRunning(project: Project): Boolean {
        val manager = LspClientManager.getInstance(project)
        val clients = manager.getClientsForProvider(GodotLspClientProvider::class.java)
        val isRunning = clients.any { it.state == LspServerState.Running }
        return isRunning
    }

    fun isLspStartingUp(project: Project): Boolean {
        val manager = LspClientManager.getInstance(project)
        val clients = manager.getClientsForProvider(GodotLspClientProvider::class.java)
        return clients.any { it.state == LspServerState.Initializing }
    }

    fun restartLsp(project: Project) {
        GodotLspProjectService.getInstance(project).queueRestart()
    }

    fun ensureLspRunning(project: Project) {
        if (!isLspRunning(project) && !isLspStartingUp(project))
            restartLsp(project)
    }
}
