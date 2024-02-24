package gdscript.completion

import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.psi.*
import com.intellij.util.ProcessingContext
import gdscript.psi.GdTypeHintNm
import gdscript.psi.GdTypes
import gdscript.reference.GdTypeHintNmReference

/**
 * ReturnType reference to classId
 */
class GdTypeHintReferenceContributor : PsiReferenceContributor() {

    override fun registerReferenceProviders(register: PsiReferenceRegistrar) {
        register.registerReferenceProvider(
            psiElement(GdTypes.TYPE_HINT_NM),
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(
                    element: PsiElement,
                    context: ProcessingContext,
                ): Array<PsiReference> {
                    if (element !is GdTypeHintNm) return emptyArray()
                    return arrayOf(GdTypeHintNmReference(element, element.project));
                }
            }
        );
    }

}
