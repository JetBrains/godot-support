package gdscript.completion.utils

import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.PlatformPatterns.psiElement
import gdscript.psi.GdTypes

object GdRefIdCompletionUtil {

    val REMOTE_REF = psiElement().withSuperParent(2, PlatformPatterns.or(
        psiElement(GdTypes.ATTRIBUTE_EX),
        psiElement(GdTypes.CALL_EX),
    ));

    val PARENT_ST_REF = psiElement()
        .withSuperParent(2, psiElement(GdTypes.PARENT_ST));

    val DIRECT_REF = psiElement(GdTypes.REF_ID_NM).andNot(REMOTE_REF);

}
