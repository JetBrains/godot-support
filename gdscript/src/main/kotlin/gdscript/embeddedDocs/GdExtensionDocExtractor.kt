package gdscript.embeddedDocs

import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.util.io.DigestUtil
import gdscript.library.GdSdkPathManager
import gdscript.polySymbols.sdk.xml.GdSdkXmlParser
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import kotlin.io.path.writeBytes

object GdExtensionDocExtractor {
    sealed interface Result {
        data object Extracted : Result
        data object NotFound : Result
        data class Error(val message: String) : Result
    }

    fun manifestId(manifestPath: Path, projectRoot: Path): String {
        val relativeManifestPath = projectRoot.relativize(manifestPath).toString()
        return DigestUtil.sha256Hex(relativeManifestPath.toByteArray(Charsets.UTF_8)).take(16)
    }

    suspend fun extractBlobDocs(
        manifestPath: Path,
        project: Project,
        features: GdExtensionManifestParser.ActiveFeatureTags,
        projectBasePath: Path? = null,
    ): Result {
        val projectRoot = projectBasePath ?: project.basePath?.let(Path::of) ?: return Result.NotFound
        val resolution = GdExtensionManifestParser.parsePath(manifestPath, projectRoot, features)
        if (resolution !is GdExtensionManifestParser.Resolution.Success) {
            if (resolution is GdExtensionManifestParser.Resolution.Failure) {
                thisLogger().debug("No GDExtension documentation blob for $manifestPath: ${resolution.reason}")
            }
            return Result.NotFound
        }

        return try {
            // Step 1: pull the raw XML doc streams out of the extension binary.
            val extraction = GdEmbeddedDocExtractor.extract(resolution.binary)
            if (extraction !is GdEmbeddedDocExtractor.Result.Found) return Result.NotFound
            val documents = extraction.streams.flatMap { GdDocXmlSplitter.split(it.bytes).documents }
            if (documents.isEmpty()) return Result.NotFound

            val manifestId = manifestId(manifestPath, projectRoot)
            val directory = GdSdkPathManager.getProjectExtensionBlobDocsDir(project, manifestId) ?: return Result.NotFound
            val parent = directory.parent ?: return Result.Error("The blob documentation directory has no parent")

            withContext(Dispatchers.IO) {
                Files.createDirectories(parent)

                // Step 2: render every document into a fresh temporary directory, next to the final one.
                // We write to a temporary directory first so a failed or cancelled extraction never leaves
                // a partially written blob docs directory behind.
                val temporaryDirectory = Files.createTempDirectory(parent, "${directory.fileName}.tmp-")
                try {
                    val allValid = documents.all { document ->
                        val temporary = temporaryDirectory.resolve("${document.className}.xml")
                        temporary.writeBytes(document.content)
                        GdSdkXmlParser.parseClass(temporary) != null
                    }
                    if (!allValid) return@withContext Result.NotFound

                    // Step 3: swap the temporary directory in for the final one. The final directory may not
                    // exist yet (first extraction), or may already hold docs from a previous extraction.
                    // Both moves are atomic renames, so readers always see either the old or the new directory,
                    // never a partial one.
                    val oldDirectory = if (Files.exists(directory)) {
                        val path = Files.createTempFile(parent, "${directory.fileName}.old-", ".tmp")
                        Files.delete(path)
                        Files.move(directory, path, StandardCopyOption.ATOMIC_MOVE)
                        path
                    } else {
                        null
                    }
                    try {
                        Files.move(temporaryDirectory, directory, StandardCopyOption.ATOMIC_MOVE)
                    } catch (e: Exception) {
                        // Restore the previous docs directory so we do not leave the project without any.
                        if (oldDirectory != null) {
                            try {
                                Files.move(oldDirectory, directory, StandardCopyOption.ATOMIC_MOVE)
                            } catch (restore: Exception) {
                                e.addSuppressed(restore)
                            }
                        }
                        throw e
                    }

                    // Step 4: the new directory is in place, so the old one (if any) is now garbage.
                    if (oldDirectory != null) {
                        try {
                            deleteRecursively(oldDirectory)
                        } catch (e: Exception) {
                            thisLogger().warn("Failed to remove old GDExtension documentation at $oldDirectory", e)
                        }
                    }

                    Result.Extracted
                } finally {
                    // The temporary directory was either moved away in step 3, or the extraction failed;
                    // either way, nothing should be left behind under its name.
                    try {
                        deleteRecursively(temporaryDirectory)
                    } catch (e: Exception) {
                        thisLogger().warn("Failed to remove temporary GDExtension documentation at $temporaryDirectory", e)
                    }
                }
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to extract GDExtension documentation")
        }
    }

    private fun deleteRecursively(path: Path) {
        if (!Files.exists(path)) return
        Files.walk(path).use { paths ->
            paths.sorted(Comparator.reverseOrder()).forEach(Files::deleteIfExists)
        }
    }
}
