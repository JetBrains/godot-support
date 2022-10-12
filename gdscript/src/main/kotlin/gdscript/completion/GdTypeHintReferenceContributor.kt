package gdscript.completion

import com.intellij.openapi.util.TextRange
import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.psi.*
import com.intellij.util.ProcessingContext
import gdscript.psi.GdTypes
import gdscript.reference.GdTypeHintReference

class GdTypeHintReferenceContributor : PsiReferenceContributor() {

    override fun registerReferenceProviders(register: PsiReferenceRegistrar) {
        register.registerReferenceProvider(
            PlatformPatterns.or(
                psiElement(GdTypes.TYPE_HINT_NM),
            ),
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(
                    element: PsiElement,
                    context: ProcessingContext,
                ): Array<PsiReference> {
                    return arrayOf(GdTypeHintReference(element, TextRange(0, element.textLength)));
                }
            }
        );
    }

}
