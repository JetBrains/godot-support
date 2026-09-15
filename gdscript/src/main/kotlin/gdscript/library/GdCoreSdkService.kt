package gdscript.library

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.progress.ProcessCanceledException
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.util.Version
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.newvfs.RefreshQueue
import com.intellij.serviceContainer.AlreadyDisposedException
import com.intellij.util.system.LowLevelLocalMachineAccess
import com.intellij.util.system.OS
import gdscript.embeddedDocs.GdCoreDocPipeline
import gdscript.embeddedDocs.GdMacBundleUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.nio.file.Path
import kotlin.io.path.isDirectory
import kotlin.io.path.name

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
     * Makes the core documentation for [version] match [godotPath].
     *
     * The service writes the stamp last, and only after the new set is visible in the VFS and the
     * trackers moved. A cancellation or a failure before that point leaves an invalid stamp, so the
     * next run retries. A failed extraction keeps the existing directory and the existing stamp.
     *
     * A degraded extraction adds its own files and deletes nothing, so it never leaves the user with less
     * documentation than before. Only a clean extraction removes a file that the new set does not name.
     */
    suspend fun ensureCoreDocs(version: Version, godotPath: Path): Result = mutex.withLock {
        val coreSdkDir = GdSdkPathManager.getCoreSdkDir(version)
        val coreSdkStampFile = GdSdkPathManager.getCoreSdkStampFile(version)
        val binary = resolveBundleExecutable(godotPath) ?: godotPath

        val stamp = GdSdkIntegrityValidator.coreStamp(version, binary)
        if (stamp == null) {
            thisLogger().warn("Cannot read the attributes of the Godot executable $binary, so the core documentation stays as it is.")
            return@withLock Result.FAILED
        }
        if (withContext(Dispatchers.IO) { GdSdkIntegrityValidator.hasValidCoreStamp(coreSdkStampFile, stamp) }) {
            return@withLock Result.UP_TO_DATE
        }

        // Stamp out of date, need to rebuild anew
        val outcome = withContext(Dispatchers.IO) {
            GdCoreDocPipeline.build(binary) { coroutineContext.ensureActive() }
        }
        val ready = when (outcome) {
            is GdCoreDocPipeline.Outcome.Rejected -> {
                val rejectionString = when (outcome.reason) {
                    GdCoreDocPipeline.Outcome.RejectReason.INPUT_UNAVAILABLE,
                    GdCoreDocPipeline.Outcome.RejectReason.NO_CANDIDATE_FOUND,
                    GdCoreDocPipeline.Outcome.RejectReason.NO_CANDIDATE_VALIDATED,
                    GdCoreDocPipeline.Outcome.RejectReason.STREAM_CAP_EXCEEDED,
                    GdCoreDocPipeline.Outcome.RejectReason.SCAN_GUARD_EXCEEDED -> {
                        val candidates = outcome.candidateCount?.let { "$it candidates" } ?: "candidate count unavailable"
                        "The extractor found no documentation in $binary: ${outcome.reason}, $candidates."
                    }
                    GdCoreDocPipeline.Outcome.RejectReason.TOTAL_CAP_EXCEEDED ->
                        "The extractor reached its total size cap on $binary: ${outcome.reason}. " +
                            "The documentation is incomplete."
                    GdCoreDocPipeline.Outcome.RejectReason.REQUIRED_CLASS_MISSING ->
                        "The documentation in $binary misses the required classes: ${outcome.missingClasses.sorted()} " +
                            "(${outcome.reason})."
                }
                thisLogger().warn("The core documentation stays as it is. $rejectionString")
                return@withLock Result.FAILED
            }
            is GdCoreDocPipeline.Outcome.Ready -> outcome
        }
        ready.recoveryReport?.let { thisLogger().warn("$it (Godot $version, $binary)") }

        // A degraded extraction may miss a class that the directory already documents, so it must not
        // delete. It adds its own files, and the next clean extraction removes what it left behind.
        val sweepStale = ready.sweepStale
        withContext(Dispatchers.IO) { GdCoreDocPipeline.writeCoreDocs(coreSdkDir, ready.files, sweepStale) }

        // The stamp must not become valid before a reader can see the new set, because a valid stamp
        // stops every later run from refreshing the directory again.
        val root = withContext(Dispatchers.IO) { VfsUtil.findFile(coreSdkDir, true) }
        if (root == null) {
            thisLogger().warn("The core documentation in $coreSdkDir is not visible in the VFS, so the stamp stays invalid.")
            return@withLock Result.FAILED
        }
        RefreshQueue.getInstance().refresh(true, listOf(root))

        currentCoroutineContext().ensureActive()
        bumpOpenProjects()

        withContext(Dispatchers.IO) { GdSdkIntegrityValidator.writeCoreStamp(coreSdkStampFile, stamp) }
        thisLogger().info(
            "Wrote ${ready.files.size} core documentation files for Godot $version into $coreSdkDir, " +
                "and ${if (sweepStale) "removed every file that the new set does not name" else "kept every earlier file"}."
        )
        return@withLock Result.WRITTEN
    }

    /**
     * Moves the symbol tracker of every open project. Each open project reads the shared directory.
     */
    private fun bumpOpenProjects() {
        for (project in ProjectManager.getInstance().openProjects) {
            if (project.isDisposed) continue
            try {
                GdSdkDocsTracker.getInstance(project).docsChanged()
            }
            catch (e: ProcessCanceledException) {
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

    /**
     * Resolves a macOS application bundle to the executable inside it.
     *
     * The extractor reads the executable, and a bundle directory holds no bytes to read.
     * A plain binary is not a bundle, so the service uses it directly. A bundle with no readable executable returns null.
     */
    @OptIn(LowLevelLocalMachineAccess::class)
    private fun resolveBundleExecutable(godotPath: Path): Path? {
        if (OS.CURRENT != OS.macOS) return null
        if (!godotPath.name.endsWith(".app") || !godotPath.isDirectory()) return null

        val result = GdMacBundleUtils.resolveAppExecutable(godotPath)
        return (result as? GdMacBundleUtils.BundleResolution.Found)?.executable
    }
}
