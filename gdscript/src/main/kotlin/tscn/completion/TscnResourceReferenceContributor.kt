package tscn.completion

import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.psi.*
import com.intellij.util.ProcessingContext
import tscn.psi.TscnTypes
import tscn.reference.TscnResourceReference

/**
 * res://path/to/file
 */
class TscnResourceReferenceContributor : PsiReferenceContributor() {

    override fun registerReferenceProviders(register: PsiReferenceRegistrar) {
        register.registerReferenceProvider(
            psiElement(TscnTypes.HEADER_VALUE_VAL),
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(
                    element: PsiElement,
                    context: ProcessingContext,
                ): Array<PsiReference> {
                    return arrayOf(TscnResourceReference(element))
                }
            }
        )
    }

}
