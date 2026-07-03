package com.jetbrains.rider.godot.community.actions

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.configurations.ParametersList
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Version
import com.intellij.util.NetworkUtils
import com.jetbrains.rider.godot.community.GdProjectGodotService
import com.jetbrains.rider.godot.community.utils.GodotCommunityUtil
import java.nio.file.InvalidPathException
import java.nio.file.Path
import kotlin.io.path.isDirectory
import kotlin.io.path.pathString

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
        val arguments = withDefaultServerPorts(launchConfig, parsedVersion)

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

        thisLogger().info("Starting Godot editor: ${runCommandLine.commandLineString}")

        // without discarding output, closing GodotEditor on mac would take several minutes
        runCommandLine.toProcessBuilder()
            .redirectError(ProcessBuilder.Redirect.DISCARD)
            .redirectOutput(ProcessBuilder.Redirect.DISCARD)
            .start()
    }

    /**
     * Ensures `--lsp-port` and `--dap-port` are present so a running editor can be discovered later
     * (see `RunningGodotEditorDiscovery`). User-supplied port values are preserved.
     *
     * `--path` may be absolute or relative to [GodotEditorLaunchConfig.workingDirectory]; in the latter
     * case it is resolved to an absolute, normalized path (this matches how Godot itself resolves the
     * argument against its cwd). We then check the resolved path is an existing directory — only then
     * do we inject the ports and hand the resolved absolute `--path` to the launched process, so that
     * `RunningGodotEditorDiscovery` (which matches by exact absolute `--path`) can find it later.
     *
     * If `--path` is missing, malformed, or does not point to an existing directory, we skip port
     * injection entirely and pass the original arguments through untouched (RIDER-140286).
     */
    internal fun withDefaultServerPorts(config: GodotEditorLaunchConfig, parsedVersion: Version?): List<String> {
        val resolvedArgs = resolveAbsolutePathArgument(config.arguments, config.workingDirectory)
        if (resolvedArgs == null) {
            thisLogger().info(
                "Skipping --lsp-port/--dap-port injection: --path is missing, malformed, or does not " +
                "point to an existing location; RunningGodotEditorDiscovery would not be able to match this process later."
            )
            return config.arguments
        }

        val params = ParametersList().apply { addAll(resolvedArgs) }
        val hasLspPort = params.hasParameter("--lsp-port")
        val hasDapPort = params.hasParameter("--dap-port")

        val supportsLspPort = parsedVersion == null || !parsedVersion.lessThan(4, 2)
        val supportsDapPort = parsedVersion == null || !parsedVersion.lessThan(4, 3)

        val needLsp = !hasLspPort && supportsLspPort
        val needDap = !hasDapPort && supportsDapPort
        if (!needLsp && !needDap) return resolvedArgs

        // Start the LSP and DAP searches from different base ports so that, when both are needed,
        // findFreePort doesn't hand out the same port twice before either is bound. The DAP search
        // also excludes the LSP port as a second safeguard.
        val result = resolvedArgs.toMutableList()
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

    /**
     * Returns [arguments] with the `--path` value normalized to an absolute path (relative values
     * are resolved against [workDir]) — but only if the resolved path is an existing directory.
     *
     * Returns `null` when `--path` is missing, has no value, cannot be parsed as a path, or the
     * resolved path is not an existing directory. Callers use `null` as a signal that the launch
     * is not discoverable by `RunningGodotEditorDiscovery` and thus port injection should be skipped.
     */
    private fun resolveAbsolutePathArgument(arguments: List<String>, workDir: Path): List<String>? {
        val idx = arguments.indexOf("--path")
        if (idx < 0 || idx + 1 >= arguments.size) return null
        val raw = arguments[idx + 1]
        val resolved = try {
            val p = Path.of(raw)
            (if (p.isAbsolute) p else workDir.resolve(p)).toAbsolutePath().normalize()
        } catch (_: InvalidPathException) {
            return null
        }
        if (!resolved.isDirectory()) return null
        return arguments.toMutableList().also { it[idx + 1] = resolved.pathString }
    }
}
