package gdscript.lsp

import com.intellij.diagnostic.rethrowControlFlowException
import com.intellij.execution.process.OSProcessUtil
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.util.execution.ParametersListUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.nio.file.InvalidPathException
import java.nio.file.Path
import java.nio.file.Paths

/**
 * Detects a running Godot editor for the given Rider project by inspecting OS processes, and returns
 * the value of a requested `--<port-flag>` argument (e.g. `--lsp-port` or `--dap-port`), if any.
 * A process matches when its executable looks like Godot, it runs as an editor (`--editor` / `-e`),
 * it has the requested `--<port-flag> <port>`, and its `--path` exactly equals basePath.
 *
 * The point is to connect to an already-running editor for the same project instead of starting a
 * new one (or using a stale port from settings).
 *
 * We match on `--path` rather than working directory because the IntelliJ Platform doesn't expose a
 * foreign process's working directory, and reading it ourselves (`/proc/<pid>/cwd`, `lsof`) is too
 * platform-specific. Both our launcher and the Godot project manager always pass `--path`.
 *
 * Process listing uses the same source as Run → Attach to Process (`OSProcessUtil.getProcessList`).
 * `ProcessHandle.Info.command` / `arguments` often miss the command line on Windows (JBR-5053, JDK-8263139).
 *
 * TODO: Consider attempt to connect Godot editor processes without `--path`
 * and somehow use `gdscript_client/changeWorkspace` notification to verify if it is the correct one
 */
object RunningGodotEditorDiscovery {
    private val LOG = Logger.getInstance(RunningGodotEditorDiscovery::class.java)

    /** Convenience wrapper looking up `--lsp-port` of a running Godot editor for [basePath]. */
    suspend fun findRunningGodotLspPort(basePath: Path): Int? = findRunningGodotPort(basePath, "--lsp-port")

    /** Convenience wrapper looking up `--dap-port` of a running Godot editor for [basePath]. */
    suspend fun findRunningGodotDapPort(basePath: Path): Int? = findRunningGodotPort(basePath, "--dap-port")

    /** Parsed information about a Godot process command-line that is relevant for editor discovery. */
    private data class GodotProcessArgs(val path: String?, val port: Int?, val isEditor: Boolean)

    /**
     * Looks up a running Godot editor for [basePath] and returns the value of its [portFlag]
     * argument (e.g. `--lsp-port`, `--dap-port`), or `null` if no matching editor is found.
     *
     * Only processes whose `--path` exactly equals [basePath] are considered; processes without
     * `--path` are ignored (see class-level TODO).
     */
    @Suppress("DEPRECATION", "DEPRECATION_ERROR")
    suspend fun findRunningGodotPort(basePath: Path, portFlag: String): Int? = withContext(Dispatchers.IO) {
        OSProcessUtil.getProcessList().firstNotNullOfOrNull { processInfo ->
            try {
                inspectGodotProcess(
                    executableName = processInfo.executableName,
                    commandLine = processInfo.commandLine,
                    args = processInfo.args,
                    basePath = basePath,
                    portFlag = portFlag,
                )
            }
            catch (e: Exception) {
                rethrowControlFlowException(e)
                LOG.debug("Cannot inspect process ${processInfo.pid}", e)
                null
            }
        }
    }

    private fun inspectGodotProcess(
        executableName: String,
        commandLine: String,
        args: String,
        basePath: Path,
        portFlag: String,
    ): Int? {
        if (!isGodotProcess(executableName, commandLine)) return null

        val argv = godotArgsFromCommandLine(commandLine, args)
        if (argv.isEmpty()) return null

        val processArgs = parseGodotArgs(argv, portFlag)
        if (!processArgs.isEditor) return null
        if (processArgs.port == null) return null
        val pathString = processArgs.path ?: return null
        val parsedPath = try {
            Paths.get(pathString)
        }
        catch (_: InvalidPathException) {
            return null
        }
        if (parsedPath.normalize() != basePath.normalize()) return null
        return processArgs.port
    }

    private fun isGodotProcess(executableName: String, commandLine: String): Boolean {
        if (looksLikeGodotExecutable(executableName)) return true
        val firstToken = ParametersListUtil.parse(commandLine).firstOrNull() ?: return false
        return looksLikeGodotExecutable(firstToken)
    }

    /**
     * Builds the Godot argv (without the executable).
     * Prefers the process [args] field; falls back to parsing [commandLine].
     */
    private fun godotArgsFromCommandLine(commandLine: String, args: String): List<String> {
        val fromArgs = args.trim()
        if (fromArgs.isNotEmpty()) {
            return ParametersListUtil.parse(fromArgs)
        }

        val trimmedCommandLine = commandLine.trim()
        if (trimmedCommandLine.isEmpty()) return emptyList()

        val tokens = ParametersListUtil.parse(trimmedCommandLine)
        if (tokens.isEmpty()) return emptyList()
        // Drop the executable token when present.
        return if (looksLikeGodotExecutable(tokens.first())) tokens.drop(1) else tokens
    }

    /**
     * Parses `--path`, the requested [portFlag], and the editor flag (`--editor` / `-e`).
     * Godot only accepts the space-separated `--flag value` form (see `main/main.cpp`), so we
     * don't handle `--flag=value`.
     */
    private fun parseGodotArgs(args: List<String>, portFlag: String): GodotProcessArgs {
        val isEditor = "--editor" in args || "-e" in args
        val path = findFlagValue(args, "--path")
        val port = findFlagValue(args, portFlag)?.toIntOrNull()
        return GodotProcessArgs(path, port, isEditor)
    }

    /** Finds a value for a `--flag value` style option, or `null` if missing. */
    private fun findFlagValue(args: List<String>, flag: String): String? {
        val index = args.indexOf(flag)
        if (index < 0) return null
        return args.getOrNull(index + 1)
    }

    private fun looksLikeGodotExecutable(command: String): Boolean {
        val name = try {
            Paths.get(command).fileName?.toString()
        }
        catch (_: InvalidPathException) {
            thisLogger().trace("Invalid path: $command")
            command.substringAfterLast('/').substringAfterLast('\\')
        } ?: return false
        return name.startsWith("godot", ignoreCase = true)
    }
}
