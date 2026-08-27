package gdscript.library

import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.util.io.DigestUtil
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.readText
import kotlin.io.path.writeText

internal object GdSdkIntegrityValidator {

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
}
