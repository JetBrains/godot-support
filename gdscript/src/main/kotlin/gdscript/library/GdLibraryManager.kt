package gdscript.library

import com.intellij.diagnostic.rethrowControlFlowException
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.CapturingProcessAdapter
import com.intellij.execution.process.OSProcessHandler
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Version
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.newvfs.RefreshQueue
import com.intellij.util.system.LowLevelLocalMachineAccess
import com.intellij.util.system.OS
import gdscript.embeddedDocs.GdDocXmlMerger
import gdscript.embeddedDocs.GdExtensionDocExtractor
import gdscript.embeddedDocs.GdExtensionManifestParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.runInterruptible
import kotlinx.coroutines.withContext
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.absolutePathString
import kotlin.io.path.deleteIfExists
import kotlin.io.path.deleteRecursively
import kotlin.io.path.exists
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.readText
import kotlin.io.path.writeText

object GdLibraryManager {

    private const val MAX_LOGGED_OUTPUT_LENGTH = 4000
    private const val DUMP_SINGLETONS_RESOURCE = "/gdscript/scripts/dump_singletons.gd"

    private class GodotProcessResult(
        val commandLine: GeneralCommandLine,
        val terminated: Boolean,
        val exitCode: Int?,
        val stdout: String,
        val stderr: String
    ) {
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
        } finally {
            if (!processHandler.isProcessTerminated) {
                processHandler.destroyProcess()
            }
        }

        currentCoroutineContext().ensureActive()
        return GodotProcessResult(
            commandLine,
            terminated,
            processHandler.exitCode,
            outputCollector.output.stdout,
            outputCollector.output.stderr
        )
    }

    private fun getGodotDoctoolCommand(
        godotPath: Path,
        workingDirectory: Path,
        outputDir: Path,
        gdextension: Boolean = false
    ): GeneralCommandLine {
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
        isAcceptable: (Path) -> Boolean = { true },
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
                thisLogger().warn(
                    "Godot doctool failed with exit code $exitCode (gdextension=$gdextension).\n" + result.describe()
                )
                return false
            }

            if (!isAcceptable(outputDir)) {
                thisLogger().warn("Godot doctool produced an unacceptable documentation set at $outputDir")
                return false
            }

            // Written last, and out of the output directory as it is now: the stamp describes the generated files too.
            GdSdkIntegrityValidator.writeStamp(directoryStampFile, stamp, outputDir)
            return true
        } catch (e: Exception) {
            rethrowControlFlowException(e)
            thisLogger().warn("Failed to run Godot doctool", e)
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
        } catch (e: Exception) {
            rethrowControlFlowException(e)
            thisLogger().warn("Failed to run Godot singletons dump", e)
            return false
        }
    }

    /**
     * Makes the doctool output visible to the VFS, then invalidates the symbol caches.
     */
    private suspend fun refreshGeneratedDocs(project: Project, dirs: List<Path>): Boolean {
        val roots = withContext(Dispatchers.IO) { dirs.mapNotNull { VfsUtil.findFile(it, true) } }
        if (roots.isEmpty()) {
            // Nothing became visible in the VFS, so invalidating the caches cannot change the result.
            thisLogger().warn("Godot doctool output is not visible in the VFS: ${dirs.joinToString { it.toString() }}")
            return false
        }

        RefreshQueue.getInstance().refresh(true, roots)

        // The refresh above is a long suspension: the project may be disposed meanwhile, and
        // incModificationCount() is not a suspension point, so cancellation would not be observed
        // before getService() throws AlreadyDisposedException.
        currentCoroutineContext().ensureActive()
        if (project.isDisposed) return false
        GdSdkDocsTracker.getInstance(project).docsChanged()
        return true
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
                rethrowControlFlowException(e)
                thisLogger().warn("Failed to clear the generated Godot docs at $docsDir", e)
            }
        }
    }

    /** Removes the blob documentation of a GDExtension once its manifest is gone. */
    @OptIn(ExperimentalPathApi::class)
    private fun pruneBlobDocs(blobRoot: Path, manifestIds: Set<String>) {
        Files.createDirectories(blobRoot)
        blobRoot.listDirectoryEntries().filter { it.fileName.toString() !in manifestIds }.forEach { it.deleteRecursively() }
    }

    private data class ResolvedManifest(val path: Path, val manifestId: String, val binary: Path)

    @OptIn(LowLevelLocalMachineAccess::class)
    private fun activeFeatureTags(): GdExtensionManifestParser.ActiveFeatureTags =
        GdExtensionManifestParser.ActiveFeatureTags(
            platformName = when (OS.CURRENT) {
                OS.Windows -> "windows"
                OS.macOS -> "macos"
                else -> "linux"
            },
            architecture = when (System.getProperty("os.arch")) {
                "aarch64", "arm64" -> "arm64"
                else -> "x86_64"
            },
            debug = true,
        )

    @OptIn(ExperimentalPathApi::class)
    private suspend fun updateBlobDocs(
        project: Project,
        resolved: ResolvedManifest,
        features: GdExtensionManifestParser.ActiveFeatureTags,
        projectBasePath: Path,
    ) {
        val directory = GdSdkPathManager.getProjectExtensionBlobDocsDir(project, resolved.manifestId) ?: return
        val marker = directory.resolve(".extraction-input.txt")
        val input = try {
            withContext(Dispatchers.IO) {
                "${Files.size(resolved.binary)}\n${Files.getLastModifiedTime(resolved.binary).toMillis()}"
            }

        } catch (e: Exception) {
            rethrowControlFlowException(e)
            thisLogger().warn("Failed to read the GDExtension binary attributes for ${resolved.path}", e)
            return
        }
        val current = withContext(Dispatchers.IO) {
            try {
                if (Files.exists(marker)) marker.readText() else null
            } catch (e: Exception) {
                rethrowControlFlowException(e)
                null
            }
        }
        if (current == input) return

        when (val result = GdExtensionDocExtractor.extractBlobDocs(resolved.path, project, features, projectBasePath)) {
            GdExtensionDocExtractor.Result.Extracted -> {
                thisLogger().info("GDExtension documentation successfully extracted for ${resolved.path}")
                withContext(Dispatchers.IO) {
                    Files.createDirectories(directory)
                    marker.writeText(input)
                }
            }

            GdExtensionDocExtractor.Result.NotFound -> {
                thisLogger().warn("GDExtension documentation not found for ${resolved.path}")
                withContext(Dispatchers.IO) {
                    if (directory.exists()) directory.deleteRecursively()
                }
            }

            is GdExtensionDocExtractor.Result.Error -> {
                thisLogger().warn("Failed to extract GDExtension documentation from ${resolved.path}: ${result.message}")
            }
        }
    }

    private fun xmlFilesByClass(root: Path): Map<String, Path> {
        if (!Files.isDirectory(root)) return emptyMap()
        val result = linkedMapOf<String, Path>()
        Files.walk(root).use { paths ->
            paths.filter { Files.isRegularFile(it) && it.fileName.toString().endsWith(".xml") }
                .sorted()
                .forEach { file -> result.putIfAbsent(file.fileName.toString().removeSuffix(".xml"), file) }
        }
        return result
    }

    @OptIn(ExperimentalPathApi::class)
    private suspend fun publishMergedDocs(
        extensionsDir: Path,
        rawDir: Path,
        blobRoot: Path,
        resolvedManifests: List<ResolvedManifest>,
        useRawDocs: Boolean,
    ) {
        val rawDocs = if (useRawDocs) xmlFilesByClass(rawDir.resolve("doc_classes")) else emptyMap()
        val blobDocs = linkedMapOf<String, Path>()
        for ((_, manifestId) in resolvedManifests.sortedBy { it.path.toString() }) {
            for ((className, file) in xmlFilesByClass(blobRoot.resolve(manifestId))) {
                blobDocs.putIfAbsent(className, file)
            }
        }

        val parent = extensionsDir.parent ?: throw IllegalStateException("The published documentation directory has no parent")
        withContext(Dispatchers.IO) {
            Files.createDirectories(parent)
            val temporaryDirectory = Files.createTempDirectory(parent, "${extensionsDir.fileName}.tmp-")
            try {
                for (className in (rawDocs.keys + blobDocs.keys).sorted()) {
                    ensureActive()
                    val raw = rawDocs[className]?.let(Files::readString)
                    val blob = blobDocs[className]?.let(Files::readString)
                    val merged = when {
                        raw != null && blob != null -> GdDocXmlMerger.merge(raw, blob)
                        raw != null -> raw
                        blob != null -> blob
                        else -> continue
                    }
                    Files.writeString(temporaryDirectory.resolve("$className.xml"), merged)
                }

                val oldDirectory = if (Files.exists(extensionsDir)) {
                    val path = Files.createTempFile(parent, "${extensionsDir.fileName}.old-", ".tmp")
                    Files.delete(path)
                    path
                } else null
                if (oldDirectory != null) {
                    Files.move(extensionsDir, oldDirectory, StandardCopyOption.ATOMIC_MOVE)
                }
                try {
                    Files.move(temporaryDirectory, extensionsDir, StandardCopyOption.ATOMIC_MOVE)
                } catch (e: Exception) {
                    rethrowControlFlowException(e)
                    if (oldDirectory != null) {
                        try {
                            Files.move(oldDirectory, extensionsDir, StandardCopyOption.ATOMIC_MOVE)
                        } catch (restore: Exception) {
                            rethrowControlFlowException(restore)
                            e.addSuppressed(restore)
                        }
                    }
                    throw e
                }
                if (oldDirectory != null) {
                    try {
                        oldDirectory.deleteRecursively()
                    } catch (e: Exception) {
                        rethrowControlFlowException(e)
                        thisLogger().warn("Failed to remove old published GDExtension documentation at $oldDirectory", e)
                    }
                }
            } finally {
                try {
                    if (temporaryDirectory.exists()) temporaryDirectory.deleteRecursively()
                } catch (e: Exception) {
                    rethrowControlFlowException(e)
                    thisLogger().warn("Failed to remove temporary published GDExtension documentation at $temporaryDirectory", e)
                }
            }
        }
    }

    /**
     * Generates whatever documentation is missing or outdated.
     *
     * The core SDK docs depend on the engine version. The GDExtension docs depend on the manifests, binaries, and raw doctool output.
     * The singletons doc depends on the engine and the installed extensions. Each output has a stamp for its inputs.
     */
    suspend fun generateSdkIfNeeded(
        version: Version,
        project: Project,
        godotPath: Path?,
        projectBasePath: Path
    ) {
        GdSdkPathManager.ensureDirectoriesExist(version, project)

        // Core and singleton refreshes are batched, so they increment the modification tracker only once.
        // The published extension docs use an immediate refresh before the merge stamp is written.
        val dirsToRefresh = mutableListOf<Path>()

        // A core failure must not stop the GDExtension or singletons generation
        generateCoreDocsIfNeeded(version, godotPath, dirsToRefresh)
        val manifestsStamp = generateExtensionDocsIfNeeded(project, godotPath, projectBasePath)
        generateSingletonDocsIfNeeded(project, godotPath, projectBasePath, version, manifestsStamp, dirsToRefresh)

        if (dirsToRefresh.isNotEmpty()) refreshGeneratedDocs(project, dirsToRefresh)
    }

    private suspend fun generateCoreDocsIfNeeded(version: Version, godotPath: Path?, dirsToRefresh: MutableList<Path>) {
        if (godotPath == null) {
            // Without the Godot program, the plugin cannot generate the core docs. The shared core docs folder can still
            // hold docs for this version from an earlier session or from another project. The refresh makes these files
            // visible and drops the cached core docs that the doc reader read before the Godot project folder was known.
            dirsToRefresh.add(GdSdkPathManager.getCoreSdkDir(version))
            return
        }

        val coreResult = try {
            GdCoreSdkService.getInstance().ensureCoreDocs(version, godotPath)
        } catch (e: Exception) {
            rethrowControlFlowException(e)
            thisLogger().warn("Failed to write the core documentation for Godot $version.", e)
            GdCoreSdkService.Result.FAILED
        }
        if (coreResult != GdCoreSdkService.Result.WRITTEN) {
            dirsToRefresh.add(GdSdkPathManager.getCoreSdkDir(version))
        }
    }

    /**
     * Generates the merged GDExtension documentation the blob docs come from libraries specified
     * in `.gdextension` manifest filess, and the raw docs come from the Godot doctool.
     *
     * [godotPath] is only needed for the doctool half. When null, blob extraction runs alone.
     *
     * @return the stamp of the scanned manifests, so that the singletons doc (also GDExtension-dependent) can be
     * invalidated together with this one, or null if the manifests could not be scanned.
     */
    private suspend fun generateExtensionDocsIfNeeded(project: Project, godotPath: Path?, projectBasePath: Path): String? {
        // Read the .gdextension manifests.
        val manifests = mutableListOf<Path>()
        val manifestsStamp = try {
            withContext(Dispatchers.IO) {
                GdSdkFingerprints.ofExtensionDeclarations(projectBasePath) { manifests.add(it) }
            }
        } catch (e: Exception) {
            rethrowControlFlowException(e)
            thisLogger().warn("Failed to scan the GDExtension manifests under $projectBasePath", e)
            return null
        }

        // Parse the manifests and resolve them to the libraries they point to.
        val features = activeFeatureTags()
        val resolvedManifests = manifests.mapNotNull { manifest ->
            when (val resolution = GdExtensionManifestParser.parsePath(manifest, projectBasePath, features)) {
                is GdExtensionManifestParser.Resolution.Success -> {
                    ResolvedManifest(
                        manifest,
                        GdExtensionDocExtractor.manifestId(manifest, projectBasePath),
                        resolution.binary,
                    )
                }

                is GdExtensionManifestParser.Resolution.Failure -> {
                    thisLogger().info("Skipping GDExtension manifest $manifest: ${resolution.reason}")
                    null
                }
            }
        }

        // Update or create the blob documentation extracted from each GDExtension library.
        for (resolved in resolvedManifests) {
            try {
                updateBlobDocs(project, resolved, features, projectBasePath)
            } catch (e: Exception) {
                rethrowControlFlowException(e)
                thisLogger().warn("Failed to update the GDExtension blob documentation for ${resolved.path}", e)
            }
        }

        val blobRoot = GdSdkPathManager.getProjectExtensionsBlobRoot(project) ?: return manifestsStamp
        val rawDir = GdSdkPathManager.getProjectExtensionsRawDir(project) ?: return manifestsStamp
        val extensionsDir = GdSdkPathManager.getProjectExtensionsDir(project) ?: return manifestsStamp
        val extensionsStampFile = GdSdkPathManager.getProjectExtensionsStampFile(project) ?: return manifestsStamp
        val mergeStampFile = GdSdkPathManager.getProjectExtensionsMergeStampFile(project) ?: return manifestsStamp

        withContext(Dispatchers.IO) {
            pruneBlobDocs(blobRoot, resolvedManifests.mapTo(mutableSetOf()) { it.manifestId })
        }

        // Update or create the documentation the doctool generates for the GDExtensions.
        var rawDocsValid = GdSdkIntegrityValidator.hasValidStamp(extensionsStampFile, manifestsStamp, rawDir)
        if (godotPath != null && !rawDocsValid) {
            clearGeneratedDocs(rawDir, extensionsStampFile)
            rawDocsValid = runGodotDoctool(
                godotPath,
                projectBasePath,
                rawDir,
                extensionsStampFile,
                manifestsStamp,
                gdextension = true,
                isAcceptable = { outputDir ->
                    manifests.isEmpty() || Files.list(outputDir.resolve("doc_classes")).use { it.findAny().isPresent }
                },
            )
        }

        // Merge the doctool and blob documentation together.
        val mergeStamp = withContext(Dispatchers.IO) {
            manifestsStamp + "\n" + GdSdkFingerprints.ofSmallFilesByContent(blobRoot) + "\n" +
                GdSdkFingerprints.ofSmallFilesByContent(rawDir)
        }
        if (!GdSdkIntegrityValidator.hasValidStamp(mergeStampFile, mergeStamp, extensionsDir)) {
            withContext(Dispatchers.IO) {
                publishMergedDocs(extensionsDir, rawDir, blobRoot, resolvedManifests, rawDocsValid)
            }
            if (refreshGeneratedDocs(project, listOf(extensionsDir))) {
                withContext(Dispatchers.IO) {
                    GdSdkIntegrityValidator.writeStamp(mergeStampFile, mergeStamp, extensionsDir)
                }
            }
        }

        return manifestsStamp
    }

    /** Generates the GDExtension singleton documentation. It depends on the engine version and the installed extensions. */
    private suspend fun generateSingletonDocsIfNeeded(
        project: Project,
        godotPath: Path?,
        projectBasePath: Path,
        version: Version,
        manifestsStamp: String?,
        dirsToRefresh: MutableList<Path>,
    ) {
        if (godotPath == null) return

        val singletonsDocDir = GdSdkPathManager.getProjectSingletonsDocDir(project) ?: return
        val singletonsDocFile = GdSdkPathManager.getProjectSingletonsDocFile(project) ?: return
        val singletonsStampFile = GdSdkPathManager.getProjectSingletonsStampFile(project) ?: return
        val singletonsStamp = "$version\n${manifestsStamp.orEmpty()}"
        if (GdSdkIntegrityValidator.hasValidStamp(singletonsStampFile, singletonsStamp, singletonsDocDir)) return

        clearGeneratedDocs(singletonsDocDir, singletonsStampFile)
        dirsToRefresh.add(singletonsDocDir)
        runGodotSingletonsDump(
            godotPath, projectBasePath, GdSdkPathManager.getSingletonsScriptFile(),
            singletonsDocFile, singletonsStampFile, singletonsStamp,
        )
    }
}
