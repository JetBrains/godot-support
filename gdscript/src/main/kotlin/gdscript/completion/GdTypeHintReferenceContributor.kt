package gdscript.completion

import com.intellij.openapi.util.TextRange
import com.intellij.patterns.PsiJavaPatterns.psiElement
import com.intellij.psi.*
import com.intellij.util.ProcessingContext
import gdscript.psi.GdTypes
import gdscript.reference.GdTypeHintReference

class GdTypeHintReferenceContributor : PsiReferenceContributor() {

    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(
            psiElement(GdTypes.TYPE_HINT_NM),
            GdTypeHintReferenceProvider()
        );
    }

}

class GdTypeHintReferenceProvider : PsiReferenceProvider() {

    override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
        return arrayOf(GdTypeHintReference(element, TextRange(0, element.textLength)));
    }

}
