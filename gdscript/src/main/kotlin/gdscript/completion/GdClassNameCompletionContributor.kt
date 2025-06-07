package gdscript.completion

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.patterns.PlatformPatterns.psiElement
import gdscript.GdKeywords
import gdscript.psi.GdClassNaming
import gdscript.utils.StringUtil.snakeToPascalCase

/**
 * Filename to class_name completion
 */
class GdClassNameCompletionContributor : CompletionContributor() {

    val CLASS_NAME_NM = psiElement()
        .afterLeaf(psiElement().withText(GdKeywords.CLASS_NAME))
        .withSuperParent(2, GdClassNaming::class.java)

    override fun fillCompletionVariants(parameters: CompletionParameters, result: CompletionResultSet) {
        if (CLASS_NAME_NM.accepts(parameters.position)) {
            val filename = parameters.originalFile.name
            result.addElement(
                GdLookup.create(filename.substring(0, filename.lastIndexOf(".")).snakeToPascalCase()),
            )
            result.stopHere()
        }
    }

}
