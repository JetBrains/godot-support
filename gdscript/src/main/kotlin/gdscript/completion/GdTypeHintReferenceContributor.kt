package gdscript.completion

import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.psi.*
import com.intellij.util.ProcessingContext
import gdscript.psi.GdTypeHintRef
import gdscript.psi.GdTypes
import gdscript.reference.GdTypeHintReference

/**
 * ReturnType reference to classId
 */
class GdTypeHintReferenceContributor : PsiReferenceContributor() {

    override fun registerReferenceProviders(register: PsiReferenceRegistrar) {
        register.registerReferenceProvider(
            psiElement(GdTypes.TYPE_HINT_REF),
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(
                    element: PsiElement,
                    context: ProcessingContext,
                ): Array<PsiReference> {
                    return arrayOf(GdTypeHintReference(element as GdTypeHintRef))
                }
            }
        );
    }

}
