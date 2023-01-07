package gdscript.utils

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import gdscript.GdFileType
import gdscript.lineMarker.GdTraitLineMarkerContributor

object GdTraitUtil {

    fun endComment(header: PsiComment): PsiComment? {
        var footer = header.nextSibling;
        while (footer != null) {
            if (footer is PsiComment) {
                if (footer.text.startsWith(GdTraitLineMarkerContributor.SUFFIX)) {
                    return footer;
                } else if (footer.text.startsWith(GdTraitLineMarkerContributor.PREFIX)) {
                    return null;
                }
            };
            footer = footer.nextSibling;
        }

        return null;
    }

    fun listUsages(resource: String, project: Project): Array<PsiComment> {
        val manager = PsiManager.getInstance(project);

        return FileTypeIndex.getFiles(GdFileType, GlobalSearchScope.allScope(project))
            .flatMap<VirtualFile, PsiComment> { file ->
                PsiTreeUtil.findChildrenOfType(
                    manager.findFile(file),
                    PsiComment::class.java,
                ).filter {
                    it.text.startsWith(GdTraitLineMarkerContributor.PREFIX)
                }
            }
            .toTypedArray();
    }

}
