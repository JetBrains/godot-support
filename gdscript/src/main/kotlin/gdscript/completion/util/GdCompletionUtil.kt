package gdscript.completion.util

import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.PsiJavaPatterns.psiElement
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiErrorElement
import com.intellij.psi.PsiWhiteSpace

object GdCompletionUtil {

    val WHITE_OR_ERROR = PlatformPatterns.or(
        psiElement(PsiErrorElement::class.java),
        psiElement(PsiWhiteSpace::class.java),
        psiElement(PsiComment::class.java)
    );

}