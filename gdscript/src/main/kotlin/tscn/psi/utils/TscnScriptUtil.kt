package tscn.psi.utils

import com.intellij.psi.PsiElement
import com.intellij.psi.search.GlobalSearchScope
import gdscript.utils.GdVirtualFileUtil.resourcePath
import tscn.index.impl.TscnResourceIndex
import tscn.psi.TscnFile

object TscnScriptUtil {

    @Deprecated("? k čemu to je?")
    fun getSceneFile(element: PsiElement): TscnFile? {
        return TscnResourceIndex.get(
            element.containingFile.virtualFile.resourcePath(),
            element.project,
            GlobalSearchScope.allScope(element.project)
        ).firstOrNull()?.containingFile as TscnFile?;
    }

}
