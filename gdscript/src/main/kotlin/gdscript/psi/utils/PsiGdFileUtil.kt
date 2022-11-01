package gdscript.psi.utils

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import gdscript.GdFileType
import gdscript.GdKeywords
import gdscript.index.impl.GdClassNamingIndex
import gdscript.psi.*
import java.io.File

object PsiGdFileUtil {

    fun listMembers(gdFile: PsiElement): List<PsiElement> {
        return PsiTreeUtil.getChildrenOfAnyType(
            gdFile,
            GdClassVarDeclTl::class.java,
            GdConstDeclTl::class.java,
            GdEnumDeclTl::class.java,
            GdMethodDeclTl::class.java,
        );
    }

    fun gdFiles(project: Project): Collection<GdFile> {
        val virtualFiles = FileTypeIndex.getFiles(GdFileType, GlobalSearchScope.allScope(project));

        return virtualFiles.map {
            PsiManager.getInstance(project).findFile(it) as GdFile
        };
    }

    fun filename(file: PsiFile): String {
        val name = file.name;

        return name.substring(0, name.length - 3);
    }

    @Deprecated("resource util?")
    fun filepath(element: PsiElement): String {
        val basePath = element.project.basePath ?: return "";
        val file = element.containingFile.originalFile;
        val directory = file.containingDirectory?.toString()?.removePrefix("PsiDirectory:") ?: return "";

        var d = "";
        if (directory.length > basePath.length) {
            d = directory.substring(basePath.length + 1) + File.separator;
        }
        val path = "$d${file.name}";

        return path.replace(File.separator, "/");
    }

    fun getGlobalFile(project: Project): PsiFile? {
        return GdClassNamingIndex.get(
            GdKeywords.GLOBAL_SCOPE,
            project,
            GlobalSearchScope.allScope(project),
        ).firstOrNull()?.containingFile;
    }

}
