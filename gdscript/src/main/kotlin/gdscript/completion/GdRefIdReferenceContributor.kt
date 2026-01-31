package gdscript.completion

import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceContributor
import com.intellij.psi.PsiReferenceProvider
import com.intellij.psi.PsiReferenceRegistrar
import com.intellij.util.ProcessingContext
import gdscript.psi.GdRefIdRef
import gdscript.psi.GdTypes
import gdscript.reference.GdClassMemberReference

/**
 * RefId reference to ClassId, Variables, Constants, ...
 */
class GdRefIdReferenceContributor : PsiReferenceContributor() {

    override fun registerReferenceProviders(register: PsiReferenceRegistrar) {
        register.registerReferenceProvider(
            psiElement(GdTypes.REF_ID_NM),
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(
                    element: PsiElement,
                    context: ProcessingContext,
                ): Array<PsiReference> {
                    return arrayOf(GdClassMemberReference(element as GdRefIdRef))
                }
            }
        )
    }

}
