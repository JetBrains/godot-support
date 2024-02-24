package gdscript.utils

import com.intellij.psi.PsiElement

object PsiReferenceUtil {

    fun PsiElement.resolveRef(): PsiElement? {
        val ref = this.references.firstOrNull() ?: return null
        if (ref.isSoft) return null

        return ref.resolve()
    }

}
