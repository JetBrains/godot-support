package gdscript.lsp

import com.intellij.execution.configurations.ParametersList
import com.intellij.openapi.diagnostic.Logger
import java.nio.file.InvalidPathException
import java.nio.file.Path
import java.nio.file.Paths

/**
 * Detects a running Godot editor for the given Rider project and returns the value of a requested
 * `--<port-flag>` argument (e.g. `--lsp-port` or `--dap-port`), if any.
 *
 * The matching is done by inspecting running OS processes:
 * - their executable file name must look like Godot;
 * - the process must be running as an editor (i.e. have the `--editor` / `-e` argument);
 * - the process must have the requested `--<port-flag> <port>` argument;
 * - **and** either the process's `--path` matches basePath, or `--path` is absent and we let the
 *   server itself confirm which project it serves (see below).
 *
 * The idea is that if a Godot editor for the same project is already running with the requested
 * server (LSP or DAP) port, we should connect to it instead of starting a new one (or instead
 * of using a stale value from settings).
 *
 * Why no working-directory matching:
 * The IntelliJ Platform does not expose a foreign process's working directory — neither
 * [ProcessHandle.Info] nor [com.intellij.execution.process.ProcessInfo] /
 * [com.intellij.execution.process.OSProcessUtil] provide it. Rolling our own with
 * `/proc/<pid>/cwd` or `lsof` is too platform-specific (Rider's Unity plugin does the same thing
 * manually too — see `UnityRunUtil.fillProjectNamesFromWorkingDirectory`). Instead we fall back
 * to the protocol itself:
 *
 * Fallback when `--path` is absent: we still report the port, and the LSP server itself signals a
 * project mismatch via the Godot-specific `gdscript_client/changeWorkspace` notification (sent
 * from `initialize` when the server's currently-open project doesn't match the client's `rootUri`).
 * Our existing [gdscript.lsp.GodotLsp4jClient] handler reacts to that notification by stopping the
 * LSP client and showing a warning.
 * TODO: Attempt to connect the next possible process without `--path`
 */
object RunningGodotEditorDiscovery {
    private val LOG = Logger.getInstance(RunningGodotEditorDiscovery::class.java)

    /** Convenience wrapper looking up `--lsp-port` of a running Godot editor for [basePath]. */
    fun findRunningGodotLspPort(basePath: Path): Int? = findRunningGodotPort(basePath, "--lsp-port")

    /** Convenience wrapper looking up `--dap-port` of a running Godot editor for [basePath]. */
    fun findRunningGodotDapPort(basePath: Path): Int? = findRunningGodotPort(basePath, "--dap-port")

    /** Parsed information about a Godot process command-line that is relevant for editor discovery. */
    data class GodotProcessArgs(val path: String?, val port: Int?, val isEditor: Boolean)

    /**
     * Looks up a running Godot editor for [basePath] and returns the value of its [portFlag]
     * argument (e.g. `--lsp-port`, `--dap-port`), or `null` if no matching editor is found.
     *
     * Two passes:
     * 1. Prefer a process whose `--path` exactly equals [basePath] (the common case — our launcher
     *    and the Godot project manager always pass `--path`).
     * 2. Otherwise, fall back to any Godot editor process that has no `--path` argument; the LSP
     *    server itself will tell the client which project it serves (via
     *    `gdscript_client/changeWorkspace`), and our `GodotLsp4jClient` disconnects on mismatch.
     */
    fun findRunningGodotPort(basePath: Path, portFlag: String): Int? {
        val normalizedBase = try {
            basePath.toAbsolutePath().normalize()
        } catch (e: Exception) {
            LOG.debug("Cannot normalize base path '$basePath'", e)
            return null
        }

        val processes = try {
            ProcessHandle.allProcesses().toList()
        } catch (e: Exception) {
            LOG.debug("Cannot list OS processes", e)
            return null
        }

        // Collect all Godot editor candidates with the requested port flag.
        val candidates = mutableListOf<Pair<Long, GodotProcessArgs>>()
        for (handle in processes) {
            val info = handle.info()
            val command = info.command().orElse(null) ?: continue
            if (!looksLikeGodotExecutable(command)) continue

            val args = info.arguments().orElse(null)?.toList() ?: continue
            val parsed = parseGodotArgs(args, portFlag)
            if (!parsed.isEditor) continue
            if (parsed.port == null) continue
            candidates += handle.pid() to parsed
        }

        // Pass 1: exact --path match.
        candidates.firstOrNull { pathMatches(it.second.path, normalizedBase) }?.let { (pid, parsed) ->
            LOG.info("Discovered running Godot pid=$pid for $normalizedBase via --path, $portFlag=${parsed.port}")
            return parsed.port
        }

        // Pass 2: process with no --path; let the server validate via gdscript_client/changeWorkspace.
        candidates.firstOrNull { it.second.path.isNullOrBlank() }?.let { (pid, parsed) ->
            LOG.info(
                "Discovered running Godot pid=$pid without --path, $portFlag=${parsed.port}; " +
                    "workspace will be validated by the LSP server (gdscript_client/changeWorkspace)"
            )
            return parsed.port
        }

        return null
    }

    /**
     * Parses the flags from Godot's argument list that are relevant for discovery: `--path`,
     * the requested [portFlag] (e.g. `--lsp-port`, `--dap-port`), and the editor flag
     * (`--editor` / `-e`).
     *
     * Uses the IntelliJ Platform [ParametersList] for presence checks and a small local helper
     * for `--flag value` value extraction (which [ParametersList] does not cover for non-`-D`
     * properties). Godot itself only accepts the space-separated form (see the `arg == "--flag"`
     * branches in Godot's `main/main.cpp`), so we don't need to handle `--flag=value`.
     */
    internal fun parseGodotArgs(args: List<String>, portFlag: String): GodotProcessArgs {
        val params = ParametersList().apply { addAll(args) }
        val isEditor = params.hasParameter("--editor") || params.hasParameter("-e")
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
            command.substringAfterLast('/').substringAfterLast('\\')
        } ?: return false
        return name.startsWith("godot", ignoreCase = true)
    }

    internal fun pathMatches(rawPath: String?, normalizedBase: Path): Boolean {
        if (rawPath.isNullOrBlank()) return false
        return try {
            val candidate = Paths.get(rawPath).toAbsolutePath().normalize()
            candidate == normalizedBase
        } catch (_: InvalidPathException) {
            false
        }
    }
}
