package gdscript.utils

import com.intellij.openapi.roots.ProjectFileIndex
import com.intellij.psi.PsiFile

object PsiFileUtil {

    fun PsiFile.isInSdk(): Boolean {
        return ProjectFileIndex.getInstance(this.project).isInLibrary(this.virtualFile)
    }

}
