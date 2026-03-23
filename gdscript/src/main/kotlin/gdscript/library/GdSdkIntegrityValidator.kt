package gdscript.library

import com.intellij.openapi.util.Version
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.pathString
import kotlin.io.path.readText
import kotlin.io.path.relativeTo
import kotlin.io.path.walk
import kotlin.io.path.writeText

internal object GdSdkIntegrityValidator {
    /*
     * Old Sdk
     */
    const val STAMP_FILE_NAME: String = "_gd-sdk-file-stamp.txt"

    fun getFilesFromFs(folder: Path): List<String> =
        folder.walk()
            .filter { it.fileName?.toString() != STAMP_FILE_NAME }
            .map { it.relativeTo(folder).pathString }
            .sorted()
            .toList()

    fun writeStamp(folder: Path) {
        val stamp = getFilesFromFs(folder).count()
        folder.resolve(STAMP_FILE_NAME).writeText("$stamp", Charsets.UTF_8)
    }

    /*
     * New sdk
     */
    // TODO change stamp system
    fun hasValidStamp(stampFile: Path, expectedVersion: Version): Boolean {
        if (!stampFile.exists()) return false
        return try {
            stampFile.readText().trim() == expectedVersion.toString()
        } catch (e: Exception) {
            false
        }
    }

    fun writeStamp(stampFile: Path, version: Version) {
        Files.createDirectories(stampFile.parent)
        stampFile.writeText(version.toString())
    }

}

