package gdscript.completion

import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.*
import com.intellij.util.ProcessingContext
import gdscript.psi.GdTypes
import gdscript.reference.GdClassMemberReference

/**
 * RefId reference to ClassId, Variables, Constants, ...
 */
class GdClassMemberReferenceContributor  : PsiReferenceContributor() {

    override fun registerReferenceProviders(register: PsiReferenceRegistrar) {
        register.registerReferenceProvider(
            PlatformPatterns.psiElement(GdTypes.REF_ID_NM),
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
                    return arrayOf(GdClassMemberReference(element));
                }
            }
        );
    }

}
