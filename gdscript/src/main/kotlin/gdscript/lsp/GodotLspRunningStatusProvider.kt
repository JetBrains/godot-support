package gdscript.lsp

import com.intellij.openapi.project.Project
import com.intellij.platform.lsp.api.LspServerManager
import com.intellij.platform.lsp.api.LspServerState

object GodotLspRunningStatusProvider {
    fun isLspRunning(project: Project): Boolean {
        val manager = LspServerManager.getInstance(project)
        val servers = manager.getServersForProvider(GodotLspServerSupportProvider::class.java)
        val isRunning = servers.any { it.state == LspServerState.Running }
        return isRunning
    }

    fun isLspStartingUp(project: Project): Boolean {
        val manager = LspServerManager.getInstance(project)
        val servers = manager.getServersForProvider(GodotLspServerSupportProvider::class.java)
        return servers.any { it.state == LspServerState.Initializing }
    }

    fun restartLsp(project: Project) {
        GodotLspProjectService.getInstance(project).queueRestart()
    }

    fun ensureLspRunning(project: Project) {
        if (!isLspRunning(project) && !isLspStartingUp(project))
            restartLsp(project)
    }
}
