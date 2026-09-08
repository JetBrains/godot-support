package gdscript.library

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.CapturingProcessAdapter
import com.intellij.execution.process.OSProcessHandler
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Version
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.newvfs.RefreshQueue
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.runInterruptible
import kotlinx.coroutines.withContext
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.absolutePathString
import kotlin.io.path.deleteIfExists
import kotlin.io.path.deleteRecursively
import kotlin.io.path.exists
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.writeText

object GdLibraryManager {

    private const val MAX_LOGGED_OUTPUT_LENGTH = 4000
    private const val DUMP_SINGLETONS_RESOURCE = "/gdscript/scripts/dump_singletons.gd"

    private class GodotProcessResult(val commandLine: GeneralCommandLine, val terminated: Boolean, val exitCode: Int?, val stdout: String, val stderr: String) {
        fun describe(): String =
            "Command line: ${commandLine.commandLineString}\n" +
            "Output:\n${stdout.takeLast(MAX_LOGGED_OUTPUT_LENGTH)}\n" +
            "Error output:\n${stderr.takeLast(MAX_LOGGED_OUTPUT_LENGTH)}"
    }

    /**
     * Starts [commandLine] and suspends until the process exits. Cancelling the caller (e.g. via the progress indicator
     * or on project close) kills the process instead of leaking it together with a blocked thread.
     */
    private suspend fun runGodotProcess(commandLine: GeneralCommandLine): GodotProcessResult {
        val processHandler = OSProcessHandler(commandLine)

        val outputCollector = CapturingProcessAdapter()
        processHandler.addProcessListener(outputCollector)
        val terminated = try {
            processHandler.startNotify()
            // ProcessHandler.waitFor() swallows the interrupt and returns false instead of throwing, so the result has to be checked explicitly.
            runInterruptible(Dispatchers.IO) { processHandler.waitFor() }
        }
        finally {
            if (!processHandler.isProcessTerminated) {
                processHandler.destroyProcess()
            }
        }

        currentCoroutineContext().ensureActive()
        return GodotProcessResult(commandLine, terminated, processHandler.exitCode, outputCollector.output.stdout, outputCollector.output.stderr)
    }

    private fun getGodotDoctoolCommand(godotPath: Path, workingDirectory: Path, outputDir: Path, gdextension: Boolean = false): GeneralCommandLine{
        val commandLine = GeneralCommandLine(godotPath.absolutePathString())
            .withWorkingDirectory(workingDirectory)
            .withParameters("--doctool", outputDir.absolutePathString())

        if (gdextension) {
            commandLine.addParameter("--gdextension-docs")
        }

        return commandLine
    }

    /**
     * Run Godot CLI with the doctool flag to generate SDK docs
     * If gdextension is true, it will generate GDExtensions docs only, else it will generate the core SDK docs
     *
     * @return true if the docs were generated successfully.
     */
    private suspend fun runGodotDoctool(
        godotPath: Path,
        workingDirectory: Path,
        outputDir: Path,
        directoryStampFile: Path,
        stamp: String,
        gdextension: Boolean = false,
    ): Boolean {
        try {
            val commandLine = getGodotDoctoolCommand(godotPath, workingDirectory, outputDir, gdextension)
            thisLogger().info("Running Godot doctool with command: ${commandLine.commandLineString} in workDir: $workingDirectory")
            val result = runGodotProcess(commandLine)
            if (!result.terminated) {
                thisLogger().warn("Godot doctool did not terminate (gdextension=$gdextension)")
                return false
            }

            val exitCode = result.exitCode
            if (exitCode != 0) {
                thisLogger().error(
                    "Godot doctool failed with exit code $exitCode (gdextension=$gdextension).\n" + result.describe()
                )
                return false
            }

            // Written last, and out of the output directory as it is now: the stamp describes the generated files too.
            GdSdkIntegrityValidator.writeStamp(directoryStampFile, stamp, outputDir)
            return true
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            thisLogger().error("Failed to run Godot doctool", e)
            return false
        }
    }

    /** The dump script lives inside the plugin jar, so Godot cannot read it directly (see [GdSdkPathManager.getSingletonsScriptFile]). */
    private suspend fun extractDumpSingletonsScript(scriptFile: Path): Boolean {
        val stream = GdLibraryManager::class.java.getResourceAsStream(DUMP_SINGLETONS_RESOURCE)
        if (stream == null) {
            thisLogger().warn("Cannot find the Godot singletons dump script at $DUMP_SINGLETONS_RESOURCE")
            return false
        }

        withContext(Dispatchers.IO) {
            Files.createDirectories(scriptFile.parent)
            stream.use { input ->
                Files.newOutputStream(scriptFile).use { output -> input.copyTo(output) }
            }
        }
        return true
    }

    /**
     * Runs the singletons dump script in a headless Godot instance and renders the reported GDExtension singletons into
     * a synthetic global scope doc file, so that they are exposed exactly like the engine singletons declared in
     * Godot's own `@GlobalScope.xml` (see [GdGlobalSingletonsDocWriter]).
     *
     * The dump runs in editor mode: editor-only extension singletons (`api_type == 3`) exist only there, and the editor
     * singleton list is a superset of the runtime one, so a single run covers both.
     *
     * @return true if the docs were regenerated, i.e. the file was written or deleted.
     */
    private suspend fun runGodotSingletonsDump(
        godotPath: Path,
        projectBasePath: Path,
        scriptFile: Path,
        docFile: Path,
        stampFile: Path,
        stamp: String,
    ): Boolean {
        try {
            if (!extractDumpSingletonsScript(scriptFile)) return false

            // --script takes an absolute path, so the script can stay in the temp directory
            val commandLine = GeneralCommandLine(godotPath.absolutePathString())
                .withWorkingDirectory(projectBasePath)
                .withParameters(
                    "--headless",
                    "--editor",
                    "--path", projectBasePath.absolutePathString(),
                    "--script", scriptFile.absolutePathString(),
                )

            thisLogger().info("Running Godot singletons dump with command: ${commandLine.commandLineString} in workDir: $projectBasePath")
            val result = runGodotProcess(commandLine)
            if (!result.terminated) {
                thisLogger().warn("Godot singletons dump did not terminate")
                return false
            }

            val exitCode = result.exitCode
            if (exitCode != 0) {
                thisLogger().error("Godot singletons dump failed with exit code $exitCode.\n" + result.describe())
                return false
            }

            // Godot exits 0 even when an extension fails to load (the error only goes to stderr), and every project has
            // engine singletons, so an empty list is the only reliable failure signal here.
            val singletons = result.stdout.lineSequence().mapNotNull { GdSingletonInfo.parseLine(it) }.toList()
            if (singletons.isEmpty()) {
                thisLogger().warn("Godot singletons dump reported no singletons.\n" + result.describe())
                return false
            }

            if (singletons.any { it.isFromGdExtension }) {
                withContext(Dispatchers.IO) {
                    Files.createDirectories(docFile.parent)
                    docFile.writeText(GdGlobalSingletonsDocWriter.buildGdExtensionScopeXml(singletons))
                }
            }
            GdSdkIntegrityValidator.writeStamp(stampFile, stamp, docFile.parent)
            return true
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            thisLogger().error("Failed to run Godot singletons dump", e)
            return false
        }
    }

    /**
     * Makes the doctool output visible to the VFS, then invalidates the symbol caches.
     */
    private suspend fun refreshGeneratedDocs(project: Project, dirs: List<Path>) {
        val roots = withContext(Dispatchers.IO) { dirs.mapNotNull { VfsUtil.findFile(it, true) } }
        if (roots.isEmpty()) {
            // Nothing became visible in the VFS, so invalidating the caches cannot change the result.
            thisLogger().warn("Godot doctool output is not visible in the VFS: ${dirs.joinToString { it.toString() }}")
            return
        }

        RefreshQueue.getInstance().refresh(true, roots)

        // The refresh above is a long suspension: the project may be disposed meanwhile, and
        // incModificationCount() is not a suspension point, so cancellation would not be observed
        // before getService() throws AlreadyDisposedException.
        currentCoroutineContext().ensureActive()
        if (project.isDisposed) return
        GdSdkDocsTracker.getInstance(project).docsChanged()
    }

    /**
     * A regeneration starts from a clean slate: the XMLs generated for an extension that has just been removed would
     * otherwise survive in the output directory. The stamp is dropped first, so that a run failing halfway leaves the
     * docs marked as outdated and is retried instead of looking up to date. The caller has to refresh [docsDir] in the
     * VFS even if the generation then fails: the old XMLs are gone from the disk already.
     */
    @OptIn(ExperimentalPathApi::class)
    private suspend fun clearGeneratedDocs(docsDir: Path, stampFile: Path) {
        withContext(Dispatchers.IO) {
            try {
                stampFile.deleteIfExists()
                if (docsDir.exists()) {
                    docsDir.listDirectoryEntries().forEach { it.deleteRecursively() }
                }
            } catch (e: Exception) {
                thisLogger().warn("Failed to clear the generated Godot docs at $docsDir", e)
            }
        }
    }

    /**
     * Generates whatever documentation is missing or outdated.
     *
     * The core SDK docs only depend on the engine version. The GDExtension docs depend on the extensions installed in
     * [projectBasePath] and on nothing else, while the generated singletons doc depends on both, because the engine
     * registers singletons of its own: each of the three is therefore stamped with what it is actually generated from,
     * and regenerated as soon as that changes.
     *
     * @param projectBasePath the Godot project directory, i.e. the one holding `project.godot`.
     */
    suspend fun generateSdkIfNeeded(
        version: Version,
        project: Project,
        godotPath: Path,
        projectBasePath: Path,
    ) {
        GdSdkPathManager.ensureDirectoriesExist(version, project)

        // Everything is refreshed at once, so the modification tracker is incremented only once.
        val dirsToRefresh = mutableListOf<Path>()

        // 1. Generate Core SDK in a centralized location (project directory)
        val coreSdkDir = GdSdkPathManager.getCoreSdkDir(version)
        val coreSdkStampFile = GdSdkPathManager.getCoreSdkStampFile(version)

        if (!GdSdkIntegrityValidator.hasValidStamp(coreSdkStampFile, version.toString(), coreSdkDir)) {
            val engineDir = godotPath.parent ?: coreSdkDir // I don't expect this to happen, but coreSdkDir is safe fallback
            if (runGodotDoctool(godotPath, engineDir, coreSdkDir, coreSdkStampFile, version.toString())) {
                dirsToRefresh.add(coreSdkDir)
            }
        }

        val extensionsStamp = withContext(Dispatchers.IO) { GdSdkFingerprints.ofExtensionDeclarations(projectBasePath) }

        // 2. Generate GDExtensions in a project-specific location
        val extensionsDir = GdSdkPathManager.getProjectExtensionsDir(project)
        val extensionsStampFile = GdSdkPathManager.getProjectExtensionsStampFile(project)

        if (extensionsDir != null && extensionsStampFile != null
            && !GdSdkIntegrityValidator.hasValidStamp(extensionsStampFile, extensionsStamp, extensionsDir)) {
            clearGeneratedDocs(extensionsDir, extensionsStampFile)
            dirsToRefresh.add(extensionsDir)
            runGodotDoctool(godotPath, projectBasePath, extensionsDir, extensionsStampFile, extensionsStamp, gdextension = true)
        }

        // 3. Dump the singletons registered by the engine: the doc XMLs carry no singleton marker at all,
        // so this is the only way to tell a GDExtension singleton from a plain Object-derived class.
        val singletonsDocDir = GdSdkPathManager.getProjectSingletonsDocDir(project)
        val singletonsDocFile = GdSdkPathManager.getProjectSingletonsDocFile(project)
        val singletonsStampFile = GdSdkPathManager.getProjectSingletonsStampFile(project)
        val singletonsStamp = "$version\n$extensionsStamp"

        if (singletonsDocDir != null && singletonsDocFile != null && singletonsStampFile != null
            && !GdSdkIntegrityValidator.hasValidStamp(singletonsStampFile, singletonsStamp, singletonsDocDir)) {
            clearGeneratedDocs(singletonsDocDir, singletonsStampFile)
            dirsToRefresh.add(singletonsDocDir)
            runGodotSingletonsDump(
                godotPath, projectBasePath, GdSdkPathManager.getSingletonsScriptFile(),
                singletonsDocFile, singletonsStampFile, singletonsStamp,
            )
        }

        if (dirsToRefresh.isNotEmpty()) {
            refreshGeneratedDocs(project, dirsToRefresh)
        }
    }
}
