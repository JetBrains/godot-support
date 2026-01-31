package tscn.completion

import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceContributor
import com.intellij.psi.PsiReferenceProvider
import com.intellij.psi.PsiReferenceRegistrar
import com.intellij.util.ProcessingContext
import tscn.psi.TscnHeaderValueVal
import tscn.psi.TscnTypes
import tscn.reference.TscnScriptClassReference

const val SCRIPT_CLASS_KEY = "script_class"

class TscnScriptClassReferenceContributor : PsiReferenceContributor() {

    private val PATTERN = psiElement(TscnTypes.HEADER_VALUE_VAL)
        .withParent(
            psiElement(TscnTypes.HEADER_VALUE)
                .withChild(
                    psiElement(TscnTypes.HEADER_VALUE_NM)
                        .withText(SCRIPT_CLASS_KEY)
                )
        )

    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) =
        registrar.registerReferenceProvider(PATTERN, TscnReferenceProvider)

    object TscnReferenceProvider : PsiReferenceProvider() {
        override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> =
            arrayOf(TscnScriptClassReference(element as TscnHeaderValueVal))
    }
}
