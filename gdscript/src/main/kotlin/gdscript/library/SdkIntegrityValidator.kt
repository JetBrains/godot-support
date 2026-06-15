package gdscript.library

import java.nio.file.Path
import kotlin.io.path.pathString
import kotlin.io.path.relativeTo
import kotlin.io.path.walk
import kotlin.io.path.writeText

internal object SdkIntegrityValidator {
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
}

