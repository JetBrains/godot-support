package gdscript.psi.utils

import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import gdscript.GdFileType
import gdscript.psi.GdLiteralEx
import gdscript.utils.VirtualFileUtil.resourcePath

/**
 * Resource "res://Losos.gd" utils
 */
object GdResourceUtil {

    /**
     * Search whole project for positions of given resource "res://" element
     */
    fun findResourcesByName(resourceFile: PsiElement): Array<PsiElement> {
        val project = resourceFile.project;
        val manager = PsiManager.getInstance(project);
        val search = resourceFile.containingFile.virtualFile.resourcePath();

        return FileTypeIndex.getFiles(GdFileType, GlobalSearchScope.allScope(project))
            .flatMap<VirtualFile, PsiElement> { file ->
                PsiTreeUtil.findChildrenOfAnyType(
                    manager.findFile(file),
                    GdLiteralEx::class.java,
                ).mapNotNull {
                    val text = it.text.trim('"');
                    if (text == search) {
                        it
                    } else {
                        null
                    }
                }
            }
            .toTypedArray();
    }

}
