package com.jetbrains.rider.godot.community.actions

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.configurations.ParametersList
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Version
import com.intellij.util.NetworkUtils
import com.jetbrains.rider.godot.community.GdProjectGodotService
import com.jetbrains.rider.godot.community.utils.GodotCommunityUtil

object StartGodotEditorAction : DumbAwareAction() {
    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        startEditor(project)
    }

    override fun update(e: AnActionEvent) {
        val project = e.project
        if (project == null) {
            e.presentation.isEnabledAndVisible = false
            return
        }

        val isGodotProject = GodotCommunityUtil.isGodotProject(project)
        val executableAvailable = GodotCommunityUtil.getGodotExecutablePath(project) != null

        e.presentation.isVisible = isGodotProject
        e.presentation.isEnabled = executableAvailable

        super.update(e)
    }

    fun startEditor(project: Project) {
        val launchConfig = GodotCommunityUtil.getEditorLaunchConfig(project) ?: return

        val parsedVersion = GdProjectGodotService.getInstance(project).projectInfoFlow.value?.parsedVersion
        val arguments = withDefaultServerPorts(launchConfig.arguments, parsedVersion)

        val runCommandLine = GeneralCommandLine(launchConfig.executablePath.toString())
            .withEnvironment(launchConfig.environmentVariables)
            .withParentEnvironmentType(
                if (launchConfig.isPassParentEnvs) {
                    GeneralCommandLine.ParentEnvironmentType.CONSOLE
                } else {
                    GeneralCommandLine.ParentEnvironmentType.NONE
                }
            )
            .withWorkingDirectory(launchConfig.workingDirectory)
            .withParameters(arguments)

        logger.info("Starting Godot editor: ${runCommandLine.commandLineString}")

        // without discarding output, closing GodotEditor on mac would take several minutes
        runCommandLine.toProcessBuilder()
            .redirectError(ProcessBuilder.Redirect.DISCARD)
            .redirectOutput(ProcessBuilder.Redirect.DISCARD)
            .start()
    }

    /**
     * Ensures `--lsp-port` and `--dap-port` are present so a running editor can be discovered later
     * (see `RunningGodotEditorDiscovery`). User-supplied values are preserved.
     */
    internal fun withDefaultServerPorts(arguments: List<String>, parsedVersion: Version?): List<String> {
        val params = ParametersList().apply { addAll(arguments) }
        val hasLspPort = params.hasParameter("--lsp-port")
        val hasDapPort = params.hasParameter("--dap-port")

        val supportsLspPort = parsedVersion == null || !parsedVersion.lessThan(4, 2)
        val supportsDapPort = parsedVersion == null || !parsedVersion.lessThan(4, 3)

        val needLsp = !hasLspPort && supportsLspPort
        val needDap = !hasDapPort && supportsDapPort
        if (!needLsp && !needDap) return arguments

        // Start the LSP and DAP searches from different base ports so that, when both are needed,
        // findFreePort doesn't hand out the same port twice before either is bound. The DAP search
        // also excludes the LSP port as a second safeguard.
        val result = arguments.toMutableList()
        var lspPort: Int? = null
        if (needLsp) {
            lspPort = NetworkUtils.findFreePort(500050)
            result += listOf("--lsp-port", lspPort.toString())
        }
        if (needDap) {
            val dapPort = NetworkUtils.findFreePort(500060, setOfNotNull(lspPort))
            result += listOf("--dap-port", dapPort.toString())
        }
        return result
    }

    private val logger = Logger.getInstance(StartGodotEditorAction::class.java)
}
