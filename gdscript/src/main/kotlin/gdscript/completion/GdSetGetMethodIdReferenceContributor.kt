package gdscript.completion

import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceContributor
import com.intellij.psi.PsiReferenceProvider
import com.intellij.psi.PsiReferenceRegistrar
import com.intellij.util.ProcessingContext
import gdscript.psi.GdTypes
import gdscript.reference.GdSetGetMethodIdReference

/**
 * Getter & Setter reference to method
 */
class GdSetGetMethodIdReferenceContributor : PsiReferenceContributor() {

    override fun registerReferenceProviders(register: PsiReferenceRegistrar) {
        register.registerReferenceProvider(
            PlatformPatterns.or(
                psiElement(GdTypes.SET_METHOD_ID_NM),
                psiElement(GdTypes.GET_METHOD_ID_NM)
            ),
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
                    return arrayOf(GdSetGetMethodIdReference(element))
                }
            }
        )
    }

}
