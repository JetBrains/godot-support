package gdscript.psi.utils

import com.intellij.openapi.project.Project
import gdscript.completion.utils.GdFileVisitor
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

    fun resourceExists(resource: String, project: Project): Boolean {
        listResourceFiles(project).forEach {
            if (resource == resourcePath(it)) {
                return true;
            }
        }

        return false;
    }

    fun findResourceFile(resource: String, project: Project): String? {
        listResourceFiles(project).forEach {
            if (resource == resourcePath(it)) {
                return it;
            }
        }

        return null;
    }

    fun resourcePath(file: String): String {
        return "res://${file.trimStart(File.separatorChar).split(File.separatorChar).joinToString(SEPARATOR)}"
    }

    fun filePath(resource: String): String {
        return resource.substring("res://".length).replace(SEPARATOR, File.separator);
    }

}
