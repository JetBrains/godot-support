package gdscript.completion

import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import com.intellij.util.ProcessingContext
import gdscript.completion.utils.GdRefIdCompletionUtil
import gdscript.reference.GdClassMemberReference
import gdscript.reference.GdTypeHintReference

class GdClassMemberReferenceContributor  : PsiReferenceContributor() {

    override fun registerReferenceProviders(register: PsiReferenceRegistrar) {
        register.registerReferenceProvider(
            GdRefIdCompletionUtil.REMOTE_REF,
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
                    return arrayOf(GdClassMemberReference(element, TextRange(0, element.textLength)));
                }
            }
        );

        register.registerReferenceProvider(
            GdRefIdCompletionUtil.DIRECT_REF,
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
                    return arrayOf(
                        GdClassMemberReference(element, TextRange(0, element.textLength)),
                        GdTypeHintReference(element, TextRange(0, element.textLength)),
                    );
                }
            }
        );
    }

}
