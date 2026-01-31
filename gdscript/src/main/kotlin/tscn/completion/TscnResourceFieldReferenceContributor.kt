package tscn.completion

import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceContributor
import com.intellij.psi.PsiReferenceProvider
import com.intellij.psi.PsiReferenceRegistrar
import com.intellij.util.ProcessingContext
import tscn.psi.TscnDataLineNm
import tscn.psi.TscnTypes
import tscn.reference.TscnResourceFieldReference

/**
 * Represents a reference to a field of a Resource class in a .tscn/.tres file.
 */
class TscnResourceFieldReferenceContributor : PsiReferenceContributor() {

    private val PATTERN = psiElement(TscnTypes.DATA_LINE_NM)

    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) =
        registrar.registerReferenceProvider(PATTERN, TscnResourceFieldReferenceProvider)

    object TscnResourceFieldReferenceProvider : PsiReferenceProvider() {
        override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> =
            arrayOf(TscnResourceFieldReference(element as TscnDataLineNm))
    }
}
