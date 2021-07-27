package gdscript.completion

import com.intellij.openapi.util.TextRange
import com.intellij.patterns.PsiJavaPatterns.psiElement
import com.intellij.psi.*
import com.intellij.util.ProcessingContext
import gdscript.psi.GdTypes
import gdscript.reference.GdInheritanceNmReference

class GdInheritanceReferenceContributor : PsiReferenceContributor() {

    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(
            psiElement(GdTypes.INHERITANCE_ID_NMI),
            GdInheritanceReferenceProvider()
        );
    }

}

class GdInheritanceReferenceProvider : PsiReferenceProvider() {

    override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
        return arrayOf(GdInheritanceNmReference(element, TextRange(0, element.textLength)));
    }

}
