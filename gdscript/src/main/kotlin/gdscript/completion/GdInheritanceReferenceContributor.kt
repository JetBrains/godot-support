package gdscript.completion

import com.intellij.openapi.util.TextRange
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.patterns.StandardPatterns.string
import com.intellij.psi.*
import com.intellij.util.ProcessingContext
import gdscript.psi.GdTypes
import gdscript.reference.GdInheritanceNmReference

class GdInheritanceReferenceContributor : PsiReferenceContributor() {

    override fun registerReferenceProviders(register: PsiReferenceRegistrar) {
        register.registerReferenceProvider(
            psiElement(GdTypes.INHERITANCE_ID_NMI),//.withoutText(string().startsWith("\"")),
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
                    return arrayOf(GdInheritanceNmReference(element, TextRange(0, element.textLength)));
                }
            }
        );
    }

}
