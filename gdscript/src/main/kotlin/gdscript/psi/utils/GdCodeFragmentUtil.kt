package gdscript.psi.utils

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import gdscript.psi.GdPsiCodeFragment

/**
 * Context-aware resolve helpers for GDScript code fragments.
 *
 * GDScript's upward declaration walk is hand-rolled and never consults [PsiElement.getContext],
 * so a fragment resolves nothing against its context file without an explicit hop.
 *
 * Every function here is an identity no-op when the containing file is not a [GdPsiCodeFragment],
 * so normal editing paths are unaffected.
 */
object GdCodeFragmentUtil {

    /** Returns the fragment context when available, otherwise `null`. */
    @JvmStatic
    fun contextOf(element: PsiElement): PsiElement? {
        val file = element.containingFile
        if (file !is GdPsiCodeFragment) return null
        return file.context
    }

    /** Returns the fragment context when available, otherwise [element]. */
    @JvmStatic
    fun effectiveElement(element: PsiElement): PsiElement {
        return contextOf(element) ?: element
    }

    /** Returns the fragment context file when available, otherwise the element's own file. */
    @JvmStatic
    fun effectiveFile(element: PsiElement): PsiFile {
        return contextOf(element)?.containingFile ?: element.containingFile
    }
}
