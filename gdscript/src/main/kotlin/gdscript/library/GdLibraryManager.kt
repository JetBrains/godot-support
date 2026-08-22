package gdscript.library

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.CapturingProcessAdapter
import com.intellij.execution.process.OSProcessHandler
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Version
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.newvfs.RefreshQueue
import com.intellij.project.stateStore
import gdscript.polySymbols.scope.GdSdkSymbolsModificationTracker
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.runInterruptible
import kotlinx.coroutines.withContext
import java.nio.file.Path
import kotlin.io.path.absolutePathString

object GdLibraryManager {

    private const val MAX_LOGGED_OUTPUT_LENGTH = 4000

    private fun getGodotDoctoolCommand(godotPath: String, workingDirectory: Path, outputDir: Path, gdextension: Boolean = false): GeneralCommandLine{
        val commandLine = GeneralCommandLine(godotPath)
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
     * Suspends until the process exits. Cancelling the caller (e.g. via the progress indicator or on project close)
     * kills the process instead of leaking it together with a blocked thread.
     *
     * @return true if the docs were generated successfully.
     */
    private suspend fun runGodotDoctool(
        version: Version,
        godotPath: String,
        workingDirectory: Path,
        outputDir: Path,
        directoryStampFile: Path?,
        gdextension: Boolean = false,
    ): Boolean {
        try {
            val commandLine = getGodotDoctoolCommand(godotPath, workingDirectory, outputDir, gdextension)
            val processHandler = OSProcessHandler(commandLine)
            // The output is needed to report why the generation failed: users only send us the logs.
            val outputCollector = CapturingProcessAdapter()
            processHandler.addProcessListener(outputCollector)
            val terminated = try {
                processHandler.startNotify()
                // ProcessHandler.waitFor() swallows the interrupt and returns false instead of throwing,
                // so the result has to be checked explicitly.
                runInterruptible(Dispatchers.IO) { processHandler.waitFor() }
            }
            finally {
                if (!processHandler.isProcessTerminated) {
                    processHandler.destroyProcess()
                }
            }

            currentCoroutineContext().ensureActive()
            if (!terminated) {
                thisLogger().warn("Godot doctool did not terminate (gdextension=$gdextension)")
                return false
            }

            val exitCode = processHandler.exitCode
            if (exitCode != 0) {
                thisLogger().error(
                    "Godot doctool failed with exit code $exitCode (gdextension=$gdextension).\n" +
                    "Command line: ${commandLine.commandLineString}\n" +
                    "Output:\n${outputCollector.output.stdout.takeLast(MAX_LOGGED_OUTPUT_LENGTH)}\n" +
                    "Error output:\n${outputCollector.output.stderr.takeLast(MAX_LOGGED_OUTPUT_LENGTH)}"
                )
                return false
            }

            directoryStampFile?.let { GdSdkIntegrityValidator.writeStamp(it, version) }
            return true
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            thisLogger().error("Failed to run Godot doctool", e)
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
        GdSdkSymbolsModificationTracker.getInstance(project).incModificationCount()
    }

    suspend fun generateSdkIfNeeded(version: Version, project: Project, godotPathString: String) {
        val projectBasePath = project.stateStore.projectBasePath

        GdSdkPathManager.ensureDirectoriesExist(version, project)

        // Directories that are up to date still need a refresh: they may come from a previous session.
        // Everything is refreshed at once, so the modification tracker is incremented only once.
        val dirsToRefresh = mutableListOf<Path>()

        // 1. Generate Core SDK in a centralized location (project directory)
        val coreSdkDir = GdSdkPathManager.getCoreSdkDir(version)
        val coreSdkStampFile = GdSdkPathManager.getCoreSdkStampFile(version)

        if (!GdSdkIntegrityValidator.hasValidStamp(coreSdkStampFile, version)) {
            thisLogger().info("Generating core SDK for Godot $version in plugin directory")
            if (runGodotDoctool(version, godotPathString, projectBasePath, coreSdkDir, coreSdkStampFile)) {
                dirsToRefresh.add(coreSdkDir)
            }
        }

        // 2. Generate GDExtensions in a project-specific location
        val extensionsDir = GdSdkPathManager.getProjectExtensionsDir(project)
        val extensionsStampFile = GdSdkPathManager.getProjectExtensionsStampFile(project)

        if (extensionsDir != null && extensionsStampFile != null) {
            if (!GdSdkIntegrityValidator.hasValidStamp(extensionsStampFile, version)) {
                thisLogger().info("Generating GDExtensions for project")
                if (runGodotDoctool(version, godotPathString, projectBasePath, extensionsDir, extensionsStampFile, gdextension = true)) {
                    dirsToRefresh.add(extensionsDir)
                }
            }
        }

        if (dirsToRefresh.isNotEmpty()) {
            refreshGeneratedDocs(project, dirsToRefresh)
        }
    }
}
