package gdscript.completion

import com.intellij.openapi.util.TextRange
import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.PsiJavaPatterns.psiElement
import com.intellij.psi.*
import com.intellij.util.ProcessingContext
import gdscript.psi.GdTypes
import gdscript.reference.GdSetGetMethodIdNmReference

class GdSetGetMethodIdReferenceContributor : PsiReferenceContributor() {

    override fun registerReferenceProviders(register: PsiReferenceRegistrar) {
        register.registerReferenceProvider(
            PlatformPatterns.or(
                psiElement(GdTypes.SET_METHOD_ID_NM),
                psiElement(GdTypes.GET_METHOD_ID_NM)
            ),
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
                    return arrayOf(GdSetGetMethodIdNmReference(element, TextRange(0, element.textLength)));
                }
            }
        );
    }

}
