package gdscript.psi

import com.intellij.psi.PsiFile

interface GdInheritanceIdRef : GdRefElement {
    val psiFile: PsiFile?

    val isClassName: Boolean
}
