package gdscript.library

import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.findFile
import com.intellij.openapi.vfs.readText
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Collectors

class SdkIntegrityValidator {
    companion object {
        const val STAMP_FILE_NAME = "_gd-sdk-file-stamp.txt"
    }

    fun getFilesFromFs(folder: Path): List<String> =
            Files.find(folder, 16, { path, attr ->
                !attr.isDirectory && path.fileName.toString() != STAMP_FILE_NAME })
                    .map { it.toFile().relativeTo(folder.toFile()).path }
                    .sorted()
                    .collect(Collectors.toList())

    fun writeStamp(folder: File) {
        val stamp = getFilesFromFs(folder.toPath()).count()
        folder.resolve(STAMP_FILE_NAME).writeText("$stamp", Charsets.UTF_8)
    }

    fun hasValidStamp(folder: VirtualFile): Boolean {
        val stampFile = folder.findFile(STAMP_FILE_NAME)
        if (stampFile == null) {
            thisLogger().warn("Stamp file does not exist at “$stampFile”.")
            return false
        }
        val stampCount = stampFile.readText().trim()

        val count = getFilesFromFs(folder.toNioPath()).count().toString()
        if (stampCount == count) {
            thisLogger().info("Stamp is valid.")
            return true
        }
        thisLogger().warn("Folder content were modified. Details: Stamp file=$stampCount, now=$count."
        )
        return false
    }
}

