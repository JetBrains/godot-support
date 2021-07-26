package gdscript.completion.contributor

import com.intellij.openapi.util.TextRange
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.*
import com.intellij.util.ProcessingContext
import gdscript.completion.reference.GdClassNameNmReference
import gdscript.psi.GdClassNameNm

class GdClassNameReferenceContributor : PsiReferenceContributor() {
// TODO
    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(GdClassNameNm::class.java),
            ClassNameRefProvider()
        );
    }

}

class ClassNameRefProvider : PsiReferenceProvider() {
    override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
        return arrayOf(GdClassNameNmReference(element, TextRange(0, element.text.length)));
    }
}