package gdscript.completion.utils

import java.io.IOException
import java.nio.file.FileVisitResult
import java.nio.file.FileVisitor
import java.nio.file.Path
import java.nio.file.attribute.BasicFileAttributes

@Deprecated("replace with FileRes index")
class GdFileVisitor : FileVisitor<Path> {

    val files: ArrayList<String> = ArrayList();

    private val basePath: Int;

    constructor(basePath: String) : super() {
        this.basePath = basePath.length;
    }

    override fun preVisitDirectory(dir: Path?, attrs: BasicFileAttributes?): FileVisitResult {
        if (dir?.fileName?.toString()?.startsWith(".") == true) {
            return FileVisitResult.SKIP_SUBTREE;
        }

        return FileVisitResult.CONTINUE;
    }

    override fun visitFile(file: Path?, attrs: BasicFileAttributes?): FileVisitResult {
        val filename = file?.toAbsolutePath().toString();
        if (!filename.endsWith(".import")
            && !filename.endsWith(".godot")
            && !filename.endsWith(".gitignore")
            && !filename.endsWith(".gitattributes")
        ) {
            this.files.add(filename.substring(basePath));
        }

        return FileVisitResult.CONTINUE;
    }

    override fun visitFileFailed(file: Path?, exc: IOException?): FileVisitResult =
        FileVisitResult.SKIP_SUBTREE;

    override fun postVisitDirectory(dir: Path?, exc: IOException?): FileVisitResult =
        FileVisitResult.CONTINUE;

}
