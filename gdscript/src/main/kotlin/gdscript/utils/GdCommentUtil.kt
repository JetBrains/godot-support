package gdscript.utils

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.nextLeaf
import gdscript.GdFileType
import gdscript.lineMarker.GdTraitLineMarkerContributor

object GdCommentUtil {

    fun endComment(header: PsiComment, prefix: String, suffix: String): PsiComment? {
        var footer = header.nextSibling
        var nested = 1

        while (footer != null) {
            if (footer is PsiComment) {
                if (footer.text.startsWith(suffix)) {
                    if (--nested == 0) return footer
                } else if (footer.text.startsWith(prefix)) {
                    nested++
                }
            }
            footer = footer.nextLeaf(true)
        }

        return null
    }

    fun endTraitComment(header: PsiComment): PsiComment? {
        return endComment(header, GdTraitLineMarkerContributor.PREFIX, GdTraitLineMarkerContributor.SUFFIX)
    }

    fun endRegionComment(header: PsiComment): PsiComment? {
        return endComment(header, "#region", "#endregion")
    }

    /**
     * Usages of Trait
     */
    fun listUsages(resource: String, project: Project): Array<PsiComment> {
        val manager = PsiManager.getInstance(project);

        return FileTypeIndex.getFiles(GdFileType.INSTANCE, GlobalSearchScope.allScope(project))
            .flatMap<VirtualFile, PsiComment> { file ->
                PsiTreeUtil.findChildrenOfType(
                    manager.findFile(file),
                    PsiComment::class.java,
                ).filter {
                    it.text.trim() == "${GdTraitLineMarkerContributor.PREFIX}$resource"
                }
            }
            .toTypedArray();
    }

}
