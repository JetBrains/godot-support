package gdscript.library

import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.util.Version
import com.intellij.util.io.DigestUtil
import gdscript.embeddedDocs.GdDocFormat
import org.jetbrains.annotations.ApiStatus
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.fileSize
import kotlin.io.path.getLastModifiedTime
import kotlin.io.path.readText
import kotlin.io.path.writeText

@ApiStatus.Internal
object GdSdkIntegrityValidator {

    /**
     * The docs are up to date only if both halves of the stamp still match: what they were generated *from*
     * ([expectedStamp], the engine version or the installed extensions) and what was generated, i.e. the files that are
     * in [generatedDocsDir] right now. An XML deleted or edited in the output directory therefore makes the docs stale
     * exactly like a changed input does.
     */
    fun hasValidStamp(stampFile: Path, expectedStamp: String, generatedDocsDir: Path): Boolean {
        return stampFile.exists() && try {
            stampFile.readText().trim() == buildStamp(expectedStamp, generatedDocsDir)
        } catch (e: Exception) {
            thisLogger().warn(e)
            false
        }
    }

    /**
     * Has to be called once the generation has finished: the output half of the stamp describes the files as they are on
     * the disk at this very moment.
     */
    fun writeStamp(stampFile: Path, stamp: String, generatedDocsDir: Path) {
        Files.createDirectories(stampFile.parent)
        stampFile.writeText(buildStamp(stamp, generatedDocsDir))
    }

    private fun buildStamp(inputStamp: String, generatedDocsDir: Path): String {
        // The input stamp is digested as well: it is a whole list of extension declarations, and only the comparison matters.
        val input = DigestUtil.sha256Hex(inputStamp.trim().toByteArray(Charsets.UTF_8))
        return "input=$input\noutput=${GdSdkFingerprints.ofGeneratedDocs(generatedDocsDir)}"
    }

    /*
     * The core documentation comes from the Godot executable, so the stamp records the executable too.
     * A new executable at the same version, or a new byte format, invalidates the stamp.
     */
    data class CoreStamp(
        val formatVersion: Int,
        val godotVersion: String,
        val binarySize: Long,
        val binaryModifiedMillis: Long,
    ) {
        fun serialize(): String =
            "format=$formatVersion\nversion=$godotVersion\nbinarySize=$binarySize\nbinaryModified=$binaryModifiedMillis\n"
    }

    /** Reads the attributes of [binary] to create the coreStamp. Returns null when the attributes are unreadable. */
    fun coreStamp(version: Version, binary: Path): CoreStamp? = try {
        CoreStamp(
            formatVersion = GdDocFormat.VERSION,
            godotVersion = version.toString(),
            binarySize = binary.fileSize(),
            binaryModifiedMillis = binary.getLastModifiedTime().toMillis(),
        )
    } catch (_: IOException) {
        null
    } catch (_: SecurityException) {
        null
    }

    fun hasValidCoreStamp(stampFile: Path, expected: CoreStamp): Boolean {
        if (!stampFile.exists()) return false
        return try {
            stampFile.readText() == expected.serialize()
        } catch (_: Exception) {
            false
        }
    }

    fun writeCoreStamp(stampFile: Path, stamp: CoreStamp) {
        Files.createDirectories(stampFile.parent)
        stampFile.writeText(stamp.serialize())
    }
}
