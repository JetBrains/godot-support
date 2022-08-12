package gdscript.completion.util

import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.PsiJavaPatterns
import gdscript.psi.GdTypes

object GdRefIdCompletionUtil {

    val REMOTE_REF = PsiJavaPatterns.psiElement().withSuperParent(2, PlatformPatterns.or(
        PsiJavaPatterns.psiElement(GdTypes.ATTRIBUTE_EX),
        PsiJavaPatterns.psiElement(GdTypes.CALL_EX),
    ));

    val PARENT_ST_REF = PsiJavaPatterns
        .psiElement()
        .withSuperParent(2, PsiJavaPatterns.psiElement(GdTypes.PARENT_ST));

    val DIRECT_REF = PsiJavaPatterns.psiElement(GdTypes.REF_ID_NM).andNot(REMOTE_REF);

}
