package gdscript.psi.utils

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope
import gdscript.GdFileType
import gdscript.psi.GdFile

object PsiGdFileUtil {

    fun gdFiles(project: Project): Collection<GdFile> {
        val virtualFiles = FileTypeIndex.getFiles(GdFileType.INSTANCE, GlobalSearchScope.allScope(project));

        return virtualFiles.map {
            PsiManager.getInstance(project).findFile(it) as GdFile
        };
    }

}
