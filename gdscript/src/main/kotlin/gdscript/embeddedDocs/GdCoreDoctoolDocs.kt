package gdscript.embeddedDocs

import com.intellij.diagnostic.rethrowControlFlowException
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.diagnostic.thisLogger
import gdscript.library.GdLibraryManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.time.withTimeoutOrNull
import kotlinx.coroutines.withContext
import java.nio.file.Files
import java.nio.file.LinkOption.NOFOLLOW_LINKS
import java.nio.file.Path
import java.time.Duration
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.absolutePathString
import kotlin.io.path.deleteRecursively

object GdCoreDoctoolDocs {
    private val TIMEOUT_MILLIS = Duration.ofMillis(60_000L)
    private const val TOTAL_LIMIT = 128L * 1024 * 1024

    /** Runs doctool on provided binary to get the SDK APIs. */
    @OptIn(ExperimentalPathApi::class)
    suspend fun build(binary: Path): GdCoreDocPipeline.Outcome = withContext(Dispatchers.IO) {
        val directory = Files.createTempDirectory("gd-core-doctool")
        try {
            // Run doctool
            val command = GeneralCommandLine(binary.absolutePathString())
                .withWorkingDirectory(directory)
                .withParameters("--headless", "--doctool", directory.absolutePathString())
            thisLogger().info("Running core doctool: ${command.commandLineString}")
            val result = withTimeoutOrNull(TIMEOUT_MILLIS) { GdLibraryManager.runGodotProcess(command) }

            // Classify
            if (result == null) {
                thisLogger().warn("The core doctool exceeded its $TIMEOUT_MILLIS ms time limit for $binary.")
            }
            else if (!result.terminated || result.exitCode != 0) {
                thisLogger().warn("The core doctool failed with exit code ${result.exitCode}. ${result.describe()}")
            }
            else {
                val outcome = collect(directory) { coroutineContext.ensureActive() }
                if (outcome is GdCoreDocPipeline.Outcome.Rejected) {
                    thisLogger().warn("The core doctool produced no usable output for $binary: $outcome")
                }
                return@withContext outcome
            }
            GdCoreDocPipeline.Outcome.Rejected(GdCoreDocPipeline.Outcome.RejectReason.INPUT_UNAVAILABLE)
        } catch (e: Exception) {
            rethrowControlFlowException(e)
            thisLogger().warn("Failed to generate core documentation with the doctool for $binary", e)
            GdCoreDocPipeline.Outcome.Rejected(GdCoreDocPipeline.Outcome.RejectReason.INPUT_UNAVAILABLE)
        } finally {
            // Keep the doctool result and cancellation when output cleanup fails.
            try {
                directory.deleteRecursively()
            }
            catch (e: Exception) {
                rethrowControlFlowException(e)
                thisLogger().warn("Failed to delete the core doctool output directory $directory", e)
            }
        }
    }

    /** Collects only the core, module, and platform class directories that the doctool writes. */
    fun collect(directory: Path, checkCanceled: () -> Unit): GdCoreDocPipeline.Outcome {
        // Collect root folders
        val roots = mutableListOf(directory.resolve("doc/classes"))
        for (group in listOf("modules", "platform")) {
            val parent = directory.resolve(group)
            if (!Files.isDirectory(parent, NOFOLLOW_LINKS)) continue
            Files.newDirectoryStream(parent).use { entries ->
                entries.filter { Files.isDirectory(it, NOFOLLOW_LINKS) }.sorted().forEach {
                    roots.add(it.resolve("doc_classes"))
                }
            }
        }

        // Bound the bytes before the shared XML checks allocate their documents.
        val documents = mutableListOf<Pair<Long, ByteArray>>()
        var allocated = 0L
        for (root in roots) {
            if (!Files.isDirectory(root, NOFOLLOW_LINKS)) continue

            Files.newDirectoryStream(root, "*.xml").use { entries ->
                for (file in entries.sorted()) {
                    checkCanceled()
                    if (!Files.isRegularFile(file, NOFOLLOW_LINKS)) continue
                    val remaining = TOTAL_LIMIT - allocated
                    val bytes = Files.newInputStream(file).use { it.readNBytes((remaining + 1).toInt()) }
                    allocated += bytes.size
                    if (allocated > TOTAL_LIMIT) {
                        return GdCoreDocPipeline.Outcome.Rejected(GdCoreDocPipeline.Outcome.RejectReason.TOTAL_CAP_EXCEEDED)
                    }
                    documents.add(documents.size.toLong() to bytes)
                }
            }
        }
        return GdCoreDocPipeline.checkDocuments(documents, documents.size, checkCanceled = checkCanceled)
    }
}
