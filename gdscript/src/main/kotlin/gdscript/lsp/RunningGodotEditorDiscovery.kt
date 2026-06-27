package gdscript.lsp

import com.intellij.openapi.diagnostic.ControlFlowException
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.diagnostic.thisLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.nio.file.InvalidPathException
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.coroutines.cancellation.CancellationException
import kotlin.jvm.optionals.getOrNull

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
    internal data class GodotProcessArgs(val path: String?, val port: Int?, val isEditor: Boolean)

    /**
     * Looks up a running Godot editor for [basePath] and returns the value of its [portFlag]
     * argument (e.g. `--lsp-port`, `--dap-port`), or `null` if no matching editor is found.
     *
     * Only processes whose `--path` exactly equals [basePath] are considered; processes without
     * `--path` are ignored (see class-level TODO).
     */
    suspend fun findRunningGodotPort(basePath: Path, portFlag: String): Int? = withContext(Dispatchers.IO) {
        ProcessHandle.allProcesses().toList().firstNotNullOfOrNull { handle ->
            try {
                val info = handle.info()
                val command = info.command().getOrNull() ?: return@firstNotNullOfOrNull null
                if (!looksLikeGodotExecutable(command)) return@firstNotNullOfOrNull null

                val args = info.arguments().getOrNull()?.toList() ?: return@firstNotNullOfOrNull null
                val processArgs = parseGodotArgs(args, portFlag)
                if (!processArgs.isEditor) return@firstNotNullOfOrNull null
                if (processArgs.port == null) return@firstNotNullOfOrNull null
                val pathString = processArgs.path ?: return@firstNotNullOfOrNull null
                val parsedPath = try { Paths.get(pathString) } catch (_: InvalidPathException) { return@firstNotNullOfOrNull null }
                if (parsedPath.normalize() != basePath.normalize()) return@firstNotNullOfOrNull null
                processArgs.port
            }
            catch (e: Exception) {
                if (e is CancellationException || e is ControlFlowException) throw e
                LOG.debug("Cannot inspect process ${handle.pid()}", e)
                null
            }
        }
    }

    /**
     * Parses `--path`, the requested [portFlag], and the editor flag (`--editor` / `-e`).
     * Godot only accepts the space-separated `--flag value` form (see `main/main.cpp`), so we
     * don't handle `--flag=value`.
     */
    internal fun parseGodotArgs(args: List<String>, portFlag: String): GodotProcessArgs {
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

    internal fun looksLikeGodotExecutable(command: String): Boolean {
        val name = try {
            Paths.get(command).fileName?.toString()
        } catch (_: InvalidPathException) {
            thisLogger().trace("Invalid path: $command")
            command.substringAfterLast('/').substringAfterLast('\\')
        } ?: return false
        return name.startsWith("godot", ignoreCase = true)
    }
}
