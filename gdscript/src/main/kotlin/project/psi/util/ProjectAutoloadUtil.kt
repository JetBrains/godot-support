package project.psi.util

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import project.psi.model.GdAutoload

object ProjectAutoloadUtil {

    fun listGlobals(project: Project): List<GdAutoload> =
        ProjectAutoloadCache.getInstance(project).getGlobals()

    fun findFromAlias(name: String, element: PsiElement): PsiFile? {
        if (name.isEmpty()) return null
        val autoloadInfoByAlias =
            ProjectAutoloadCache.getInstance(element.project).getAutoloadInfoByAlias(name) ?: return null
        if (!autoloadInfoByAlias.enabled) return null
        return autoloadInfoByAlias.element
    }
}
