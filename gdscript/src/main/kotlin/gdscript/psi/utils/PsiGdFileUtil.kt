package gdscript.psi.utils

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope
import gdscript.GdFileType
import gdscript.competion.staticLoader.StaticClassLoader
import gdscript.index.Indices
import gdscript.psi.GdFile
import java.util.stream.Stream

object PsiGdFileUtil {

    fun gdFiles(project: Project): Collection<GdFile> {
        val virtualFiles = FileTypeIndex.getFiles(GdFileType.INSTANCE, GlobalSearchScope.allScope(project));

        return virtualFiles.map {
            PsiManager.getInstance(project).findFile(it) as GdFile
        };
    }

    // TODO povyházet
//    fun customs(project: Project): Collection<String> =
//        Indices.keys(Indices.FILE_NAMES, project)
//
//    fun customs(parameters: CompletionParameters): Collection<String> =
//        customs(parameters.position.project);
//
//    fun statics(): Collection<String> =
//        StaticClassLoader.getClasses().keys;
//
//    fun all(project: Project): Stream<String> =
//        Stream.concat(
//            statics().stream(),
//            customs(project).stream()
//        )
//
//    fun myFilename(parameters: CompletionParameters): String {
//        val name = parameters.originalFile.name;
//        return name.substring(0, name.length - 3)
//    }

}
