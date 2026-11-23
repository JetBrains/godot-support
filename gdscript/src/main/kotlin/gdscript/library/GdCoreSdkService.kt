package gdscript.library

import com.intellij.diagnostic.rethrowControlFlowException
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.progress.ProcessCanceledException
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.util.Version
import com.intellij.openapi.vfs.VfsUtil.findFile
import com.intellij.openapi.vfs.newvfs.RefreshQueue
import com.intellij.serviceContainer.AlreadyDisposedException
import gdscript.embeddedDocs.GdCoreDocPipeline
import gdscript.embeddedDocs.GdCoreDoctoolDocs
import gdscript.library.GdSdkIntegrityValidator.CoreDocsMethod
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.nio.file.Files
import java.nio.file.Files.deleteIfExists
import java.nio.file.Path

/**
 * Writes the core Godot documentation, which every open project in the process shares.
 *
 * [GdSdkPathManager.getCoreSdkDir] has no project part, so two projects can name the same directory
 * for the same `major.minor` version while they point at different executables. The service is
 * application scoped, and its [mutex] serializes every write and every delete in that directory.
 * A successful write also moves the symbol tracker of every open project, because a project that
 * did not write still reads the directory.
 */
@Service(Service.Level.APP)
class GdCoreSdkService {

    companion object {
        fun getInstance(): GdCoreSdkService = service()

        // Describe the usable set even when no executable is configured.
        fun describeCoreDocs(directory: Path, method: CoreDocsMethod?): String {
            val usable = GdCoreDocPipeline.REQUIRED_CLASS_NAMES.all { Files.isRegularFile(directory.resolve("$it.xml")) }
            return if (!usable) "none" else method?.name?.lowercase() ?: "unknown"
        }
    }

    /** Guards the shared core directory against every other project in this process. */
    private val mutex = Mutex()

    enum class Result {
        /** The directory holds a new set. The service refreshed the VFS and moved the trackers. */
        WRITTEN,

        /** The stamp matches the executable, so the directory needs no write. */
        UP_TO_DATE,

        /** The service wrote nothing. The previous set, if any, stays in use. */
        FAILED,
    }

    /**
     * Publishes checked core documentation under the shared lock and writes the stamp after the VFS refresh.
     * A matching binary stamp skips extraction. A usable folder suppresses the doctool after a binary rejection.
     */
    suspend fun ensureCoreDocs(
        version: Version,
        godotPath: Path,
        doctool: suspend (Path) -> GdCoreDocPipeline.Outcome = GdCoreDoctoolDocs::build,
    ): Result = mutex.withLock {
        val directory = GdSdkPathManager.getCoreSdkDir(version)
        val stampFile = GdSdkPathManager.getCoreSdkStampFile(version)
        val binary = godotPath
        var exactMethod: String? = null
        var binaryRejection: String? = null
        var doctoolRan = false

        // Report the attempted method and the kind that readers can use.
        suspend fun finish(result: Result, method: CoreDocsMethod?, reason: String? = null): Result {
            val kind = withContext(Dispatchers.IO) {
                describeCoreDocs(directory, GdSdkIntegrityValidator.readCoreMethod(stampFile))
            }
            val details = listOfNotNull(binaryRejection, reason).joinToString(", ")
            val resultText = if (details.isEmpty()) result.toString() else "$result ($details)"
            val warning = if (kind == "none") " GDScript types will not resolve." else ""
            val message =
                "Core documentation result=$resultText, method=${exactMethod ?: method?.name?.lowercase() ?: "unknown"}, kind=$kind, " +
                    "binary=$binary, SDK version=$version.$warning"
            if (doctoolRan) thisLogger().error(message) else thisLogger().info(message)
            return result
        }

        val previousMethod = withContext(Dispatchers.IO) { GdSdkIntegrityValidator.readCoreMethod(stampFile) }
        val stamp = withContext(Dispatchers.IO) { GdSdkIntegrityValidator.coreStamp(version, binary) }
        if (stamp == null) {
            return@withLock finish(Result.FAILED, previousMethod, "cannot read binary attributes")
        }
        // 1. Cache hit: require the binary identity and the four required class files.
        val hasRequiredClasses = withContext(Dispatchers.IO) {
            GdCoreDocPipeline.REQUIRED_CLASS_NAMES.all { Files.isRegularFile(directory.resolve("$it.xml")) }
        }
        if (hasRequiredClasses && withContext(Dispatchers.IO) { GdSdkIntegrityValidator.hasValidCoreStamp(stampFile, stamp) }) {
            return@withLock finish(Result.UP_TO_DATE, CoreDocsMethod.BINARY)
        }

        // 2. Try binary extraction to get full documentation.
        var method = CoreDocsMethod.BINARY
        val outcome = when (val extracted = withContext(Dispatchers.IO) {
            GdCoreDocPipeline.build(binary, version) { coroutineContext.ensureActive() }
        }) {
            is GdCoreDocPipeline.Outcome.Ready -> extracted
            is GdCoreDocPipeline.Outcome.Rejected -> {
                binaryRejection = "binary rejected: ${describeRejection(extracted)}"

                // 3. Binary rejected: keep usable docs, or run the doctool fallback.
                if (hasRequiredClasses && withContext(Dispatchers.IO) { Files.isRegularFile(stampFile) }) {
                    return@withLock finish(Result.FAILED, method, "doctool skipped because usable docs exist")
                }
                method = CoreDocsMethod.DOCTOOL
                exactMethod = "doctool"
                doctoolRan = true
                try {
                    doctool(binary)
                } catch (e: Exception) {
                    rethrowControlFlowException(e)
                    return@withLock finish(Result.FAILED, method, "doctool failed: $e")
                }
            }
        }
        // Check that either binary or doctool method succeeded
        val ready = when (outcome) {
            is GdCoreDocPipeline.Outcome.Rejected -> {
                return@withLock finish(Result.FAILED, method, "${method.name.lowercase()} rejected: ${describeRejection(outcome)}")
            }

            is GdCoreDocPipeline.Outcome.Ready -> outcome
        }
        exactMethod = ready.method ?: exactMethod
        ready.recoveryReport?.let { thisLogger().warn("$it (method=${method.name.lowercase()}, Godot $version, $binary)") }

        // 4. Publish in place, then refresh readers and write the stamp last.
        try {
            // Invalidate the stamp before writing the new files.
            withContext(Dispatchers.IO) {
                deleteIfExists(stampFile)
                GdCoreDocPipeline.writeCoreDocs(directory, ready.files, ready.sweepStale)
            }

            // Make the files visible and move the trackers before writing the stamp.
            val root = withContext(Dispatchers.IO) { findFile(directory, true) }
            if (root == null) {
                return@withLock finish(Result.FAILED, method, "publication failed: $directory is not visible in the VFS")
            } else {
                RefreshQueue.getInstance().refresh(true, listOf(root))
                currentCoroutineContext().ensureActive()
                bumpOpenProjects()
                withContext(Dispatchers.IO) { GdSdkIntegrityValidator.writeCoreStamp(stampFile, stamp.copy(method = method)) }
                return@withLock finish(Result.WRITTEN, method)
            }
        } catch (e: Exception) {
            rethrowControlFlowException(e)
            return@withLock finish(Result.FAILED, method, "publication failed: $e")
        }
    }

    private fun describeRejection(outcome: GdCoreDocPipeline.Outcome.Rejected): String =
        "${outcome.reason}, candidates=${outcome.candidateCount}, missing classes=${outcome.missingClasses.sorted()}"

    /**
     * Moves the symbol tracker of every open project. Each open project reads the shared directory.
     */
    private fun bumpOpenProjects() {
        for (project in ProjectManager.getInstance().openProjects) {
            if (project.isDisposed) continue
            try {
                GdSdkDocsTracker.getInstance(project).docsChanged()
            } catch (e: ProcessCanceledException) {
                // A project can close between the check above and this lookup. It throws
                // AlreadyDisposedException directly, or a plain ProcessCanceledException when the
                // caller runs under an indicator or a job. Skip only that case; re-throw any other
                // cancellation so the stamp stays invalid.
                if (e !is AlreadyDisposedException && !project.isDisposed) throw e
                val details = e.message?.let { ": $it" } ?: ""
                thisLogger().debug("Cannot move the symbol tracker of a closed project (${e.javaClass.name}$details)")
            }
        }
    }
}
