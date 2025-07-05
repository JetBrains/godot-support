package project.psi.util

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import project.index.impl.ProjectSectionIndex
import project.psi.ProjectData

object ProjectInputUtil {

    fun listActions(element: PsiElement): Iterable<String> {
        val section = ProjectSectionIndex.INSTANCE.getGlobally(ProjectSectionIndex.INPUT_SECTION, element)
            .firstOrNull() ?: return arrayListOf()
        val inputs = PsiTreeUtil.getStubChildrenOfTypeAsList(section, ProjectData::class.java)

        return inputs.map { it.key }
    }

}
