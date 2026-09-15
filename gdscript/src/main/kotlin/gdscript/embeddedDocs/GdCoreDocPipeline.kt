package gdscript.embeddedDocs

import com.intellij.openapi.progress.ProgressManager
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.deleteRecursively
import kotlin.io.path.isDirectory
import kotlin.io.path.name
import kotlin.io.path.writeBytes

object GdCoreDocPipeline {

    /** A usable set must describe *at least* these classes. The GDScript language file uses the `@` prefix. */
    val REQUIRED_CLASS_NAMES: Set<String> = setOf("Object", "Node", "RefCounted", "@GDScript")

    private const val MAX_REPORTED_ITEMS = 10

    /** One file for the core directory. [fileName] holds the class name and the `.xml` suffix. */
    data class ClassFile(val fileName: String, val bytes: ByteArray) {
        override fun equals(other: Any?): Boolean =
            other is ClassFile && fileName == other.fileName && bytes.contentEquals(other.bytes)

        override fun hashCode(): Int = 31 * fileName.hashCode() + bytes.contentHashCode()
    }

    sealed interface Outcome {
        enum class RejectReason {
            INPUT_UNAVAILABLE,
            NO_CANDIDATE_FOUND,
            NO_CANDIDATE_VALIDATED,
            STREAM_CAP_EXCEEDED,
            TOTAL_CAP_EXCEEDED,
            SCAN_GUARD_EXCEEDED,
            REQUIRED_CLASS_MISSING,
        }

        /**
         * The set is usable. [files] is sorted by file name.
         *
         * [recoveryReport] contains every recovery note. A note can name a document that the pipeline dropped or skipped.
         * A note can also report a dropped stream without naming a document. It is null when the pipeline recorded no recovery note.
         * [sweepStale] is true when the pipeline recorded no recovery note.
         * The pipeline does not verify that the class set is complete. Nothing refuses a publish, so a degraded
         * set still reaches the directory beside the files that the extraction missed.
         */
        data class Ready(
            val files: List<ClassFile>,
            val candidateCount: Int,
            val sweepStale: Boolean,
            val recoveryReport: String? = null,
        ) : Outcome {
            init {
                require(sweepStale == (recoveryReport == null)) { "sweepStale must match recoveryReport" }
            }
        }

        /**
         * The set is not usable. [candidateCount] contains the extractor count when it found no usable stream.
         * When no stream is accepted, acceptedTotal is zero. Therefore the extractor can report only a per-stream cap.
         * [missingClasses] contains missing required classes only for [RejectReason.REQUIRED_CLASS_MISSING].
         * It stays empty for every other rejection reason.
         */
        data class Rejected(
            val reason: RejectReason,
            val candidateCount: Int? = null,
            val missingClasses: Set<String> = emptySet(),
        ) : Outcome
    }

    /**
     * Builds the core class documentation from the documentation that a Godot executable embeds.
     *
     * The pipeline recovers per class. It keeps every good document. It drops a malformed document and a
     * document whose class name cannot be a file name. When two documents name the same class with different
     * content, it keeps the one at the lowest offset and skips the later one. It reports every such note.
     *
     * It rejects the whole set only when the extraction gives nothing usable, when a size cap cut the
     * extraction short, or when a required base class is absent.
     *
     * A degraded extraction must not delete. [writeCoreDocs] takes the sweep as a parameter for that reason.
     */
    fun build(binary: Path, checkCanceled: () -> Unit = { ProgressManager.checkCanceled() }): Outcome {
        val extracted = when (val result = GdEmbeddedDocExtractor.extract(binary, checkCanceled)) {
            is GdEmbeddedDocExtractor.Result.NotFound ->
                return Outcome.Rejected(
                    reason = when (result.reason) {
                        GdEmbeddedDocExtractor.NotFoundReason.INPUT_UNAVAILABLE -> Outcome.RejectReason.INPUT_UNAVAILABLE
                        GdEmbeddedDocExtractor.NotFoundReason.NO_CANDIDATE_FOUND -> Outcome.RejectReason.NO_CANDIDATE_FOUND
                        GdEmbeddedDocExtractor.NotFoundReason.NO_CANDIDATE_VALIDATED -> Outcome.RejectReason.NO_CANDIDATE_VALIDATED
                        GdEmbeddedDocExtractor.NotFoundReason.CAP_EXCEEDED ->
                            // When no stream is accepted, acceptedTotal is zero. Therefore totalRemaining exceeds STREAM_LIMIT,
                            // so only the per-stream cap can fire.
                            Outcome.RejectReason.STREAM_CAP_EXCEEDED
                        GdEmbeddedDocExtractor.NotFoundReason.SCAN_GUARD_EXCEEDED -> Outcome.RejectReason.SCAN_GUARD_EXCEEDED
                    },
                    candidateCount = result.candidateCount,
                )
            is GdEmbeddedDocExtractor.Result.Found -> result
        }

        // A cap cuts the stream set short, so the remaining classes are unknown and the set is not usable.
        if (extracted.acceptedTotalExceeded) {
            return Outcome.Rejected(
                reason = Outcome.RejectReason.TOTAL_CAP_EXCEEDED,
            )
        }

        val byName = LinkedHashMap<String, GdDocXmlSplitter.ClassDocument>()
        val notes = mutableListOf<String>()
        if (extracted.streamCapExceeded) {
            notes.add("the extractor dropped at least one stream because it exceeded the per-stream cap, so the class set may be incomplete")
        }
        for (stream in extracted.streams) {
            checkCanceled()
            val split = GdDocXmlSplitter.split(stream.bytes, checkCanceled)
            split.malformedDocuments.forEach {
                notes.add("dropped a malformed document at stream ${stream.fileOffset}, offset ${it.blobOffset}: ${it.message}")
            }
            split.conflicts.forEach {
                notes.add(
                    "kept the first variant of class ${it.className} in stream ${stream.fileOffset}, " +
                        "and skipped a later variant: ${it.firstContentHash} against ${it.conflictingContentHash}"
                )
            }
            for (document in split.documents) {
                val known = byName.putIfAbsent(document.className, document)
                if (known != null && known.contentHash != document.contentHash) {
                    notes.add(
                        "kept the first variant of class ${document.className}, which differs between two streams, " +
                            "and skipped the later variant: ${known.contentHash} against ${document.contentHash}"
                    )
                }
            }
        }

        val files = mutableListOf<ClassFile>()
        for (document in byName.values) {
            checkCanceled()
            val fileName = fileNameFor(document.className)
            if (fileName == null) {
                notes.add("dropped the document of class ${document.className}, because the name is not a file name")
                continue
            }
            files.add(ClassFile(fileName, document.content))
        }

        val presentNames = files.mapTo(HashSet()) { it.fileName.removeSuffix(".xml") }
        val missing = REQUIRED_CLASS_NAMES - presentNames
        if (missing.isNotEmpty()) {
            return Outcome.Rejected(
                reason = Outcome.RejectReason.REQUIRED_CLASS_MISSING,
                missingClasses = missing,
            )
        }

        files.sortBy { it.fileName }
        return Outcome.Ready(
            files = files,
            candidateCount = extracted.candidateCount,
            sweepStale = notes.isEmpty(),
            recoveryReport = notes.reportNotes(),
        )
    }

    /**
     * The deletion is the only step that can leave the user with less documentation, so it is optional.
     * A clean extraction passes true for [sweepStale], and the directory then holds exactly [files].
     * A degraded extraction passes false, so every file that the extraction missed stays on disk.
     * The next clean extraction removes what a degraded one left behind.
     */
    @OptIn(ExperimentalPathApi::class)
    fun writeCoreDocs(directory: Path, files: List<ClassFile>, sweepStale: Boolean) {
        Files.createDirectories(directory)
        for (file in files) {
            directory.resolve(file.fileName).writeBytes(file.bytes)
        }
        if (!sweepStale) return

        val expected = files.mapTo(HashSet()) { it.fileName }
        var allEntries: List<Path> = listOf()
        Files.newDirectoryStream(directory).use { entries ->
            for (entry in entries) {
                // Get rid of previous xml files, which were stored in directories before
                if (entry.isDirectory()) entry.deleteRecursively()
                if (entry.name !in expected) Files.deleteIfExists(entry)
                allEntries = allEntries.plusElement(entry)
            }
        }
    }


    /** Returns the file name for a class, or null when the class name cannot be a file name. */
    fun fileNameFor(className: String): String? {
        if (className.isEmpty() || className == "." || className == "..") return null
        if (className.any { it.isSafeInFileName().not() }) return null
        return "$className.xml"
    }

    private fun Char.isSafeInFileName(): Boolean =
        this.isLetterOrDigit() || this == '_' || this == '-' || this == '@' || this == '.'

    private fun List<String>.reportNotes(): String? {
        if (isEmpty()) return null
        val more = if (size > MAX_REPORTED_ITEMS) "; and ${size - MAX_REPORTED_ITEMS} more" else ""
        return "The pipeline recovered per class and wrote $size notes: " + take(MAX_REPORTED_ITEMS).joinToString("; ") + more
    }
}
