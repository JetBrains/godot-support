package gdscript.completion.utils

import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.PlatformPatterns.psiElement
import gdscript.psi.GdFile
import gdscript.psi.GdTypes

object GdRefIdCompletionUtil {

    val REMOTE_REF = psiElement().withSuperParent(2, PlatformPatterns.or(
        psiElement(GdTypes.ATTRIBUTE_EX),
        psiElement(GdTypes.CALL_EX),
    ))

    val CLASS_ROOT = psiElement().withSuperParent(2, PlatformPatterns.or(
        psiElement(GdTypes.CLASS),
        psiElement(GdFile::class.java),
    ))

    val DIRECT_REF = psiElement().withParent(psiElement(GdTypes.REF_ID_NM)).andNot(REMOTE_REF)

}
