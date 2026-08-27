package gdscript.library

import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.platform.eel.fs.EelFiles
import com.intellij.util.io.DigestUtil
import java.io.IOException
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes
import java.util.SortedSet

/**
 * Describes what the SDK documentation is generated *from* and what was actually generated, so that
 * [GdSdkIntegrityValidator] can tell whether the docs on disk are still up to date.
 */
object GdSdkFingerprints {

    const val GDEXTENSION_SUFFIX: String = ".gdextension"

    /**
     * The engine cache holds copies of the declarations and is rewritten all the time, and the IDE/VCS metadata (the
     * generated docs included) holds nothing the engine would load. Build outputs are *not* skipped: `bin` is where the
     * godot-cpp based extensions - i.e. most of them - keep their declaration.
     */
    val SKIPPED_DIRECTORY_NAMES: Set<String> = setOf(".godot", ".git", ".idea", ".vs")

    private const val MAX_DEPTH = 10

    /** A declaration is a handful of ini lines; anything larger is not read, its size and timestamp will do. */
    private const val MAX_HASHED_SIZE = 1024L * 1024L

    /**
     * Identifies the GDExtensions installed in a Godot project: the engine generates their documentation out of the
     * `*.gdextension` declarations found there and out of nothing else, so their locations and contents are the whole
     * input of that generation. Contents rather than timestamps, because cloning a repository or switching a branch
     * rewrites the timestamps of files that did not change at all.
     */
    fun ofExtensionDeclarations(projectBasePath: Path): String {
        val declarations = walk(projectBasePath, SKIPPED_DIRECTORY_NAMES) { file, attrs ->
            val name = file.fileName?.toString()
            if (name != null && name.endsWith(GDEXTENSION_SUFFIX, ignoreCase = true)) describeContent(file, attrs) else null
        }
        return declarations?.joinToString("\n") ?: ""
    }

    /**
     * Identifies the documentation XMLs that were generated into a doctool output directory: it is a plain directory on
     * disk that anything may touch, so an XML deleted by hand or a run killed halfway must make the docs as stale as a
     * changed input does. A missing directory holds the same as an empty one and is described the same way.
     *
     * Metadata rather than contents: the core SDK alone is a few thousand XMLs that are generated locally and never
     * carried around, so their size and timestamp tell an edit apart just as reliably and far more cheaply.
     */
    fun ofGeneratedDocs(docsDir: Path): String {
        val files = walk(docsDir) { _, attrs -> "${attrs.size()}|${attrs.lastModifiedTime().toMillis()}" }
        // An unreadable output directory must not look up to date.
            ?: return "unreadable"
        // Digested: a doctool output is thousands of files, and only the comparison matters.
        return DigestUtil.sha256Hex(files.joinToString("\n").toByteArray(Charsets.UTF_8))
    }

    /**
     * Collects `<path relative to [root]>|<description>` for every file [describe] returns a description for, or null
     * if [root] could not be scanned at all.
     */
    private fun walk(
        root: Path,
        skippedDirectoryNames: Set<String> = emptySet(),
        describe: (Path, BasicFileAttributes) -> String?,
    ): SortedSet<String>? {
        // Sorted: the traversal order is not guaranteed, while the fingerprint has to be stable.
        val entries = sortedSetOf<String>()
        if (!Files.exists(root)) return entries

        try {
            Files.walkFileTree(root, emptySet(), MAX_DEPTH, object : SimpleFileVisitor<Path>() {
                override fun preVisitDirectory(dir: Path, attrs: BasicFileAttributes): FileVisitResult {
                    val name = dir.fileName?.toString()
                    if (dir != root && name != null && name in skippedDirectoryNames) return FileVisitResult.SKIP_SUBTREE
                    return FileVisitResult.CONTINUE
                }

                override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
                    describe(file, attrs)?.let { entries.add("${root.relativize(file)}|$it") }
                    return FileVisitResult.CONTINUE
                }

                override fun visitFileFailed(file: Path, exc: IOException): FileVisitResult = FileVisitResult.CONTINUE
            })
        } catch (e: IOException) {
            thisLogger().warn("Failed to scan $root", e)
            return null
        }
        return entries
    }

    private fun describeContent(file: Path, attrs: BasicFileAttributes): String {
        if (attrs.size() <= MAX_HASHED_SIZE) {
            try {
                return DigestUtil.sha256Hex(EelFiles.readAllBytes(file))
            } catch (e: IOException) {
                thisLogger().warn("Failed to read the GDExtension declaration $file", e)
            }
        }
        // An unreadable or implausibly large declaration is described by its metadata: a false regeneration is cheaper
        // than docs staying stale forever.
        return "${attrs.size()}|${attrs.lastModifiedTime().toMillis()}"
    }
}
