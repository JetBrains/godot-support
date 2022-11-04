package gdscript.psi.utils

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import gdscript.completion.utils.GdFileVisitor
import gdscript.utils.VirtualFileUtil.localPath
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

object PsiGdResourceUtil {

    val SEPARATOR = "/";

    fun listResourceFiles(project: Project, onlyGdFiles: Boolean = false): Array<String> {
        val base = project.basePath ?: return emptyArray();
        val v = GdFileVisitor(base);

        Files.walkFileTree(Paths.get(base), v);

        return v.files.mapNotNull {
            if (!onlyGdFiles || it.endsWith(".gd")) {
                it
            } else {
                null
            }
        }.toTypedArray();
    }

    @Deprecated("local path by se měl řešit tady - volej metodu rovnou na virtual")
    fun resourcePath(file: String): String {
        return "res://${file.trimStart(File.separatorChar).split(File.separatorChar).joinToString(SEPARATOR)}"
    }

    fun resourcePath(file: VirtualFile): String {
        return "res://${file.localPath().trimStart(File.separatorChar).split(File.separatorChar).joinToString(SEPARATOR)}"
    }

    fun filePath(resource: String): String {
        return resource.substring("res://".length).replace(SEPARATOR, File.separator);
    }

    fun absoluteFilePath(resource: String, project: Project): String {
        val local = filePath(resource);

        return "${project.basePath}${File.separator}$local";
    }

}
