package gdscript.completion

import GdScriptPluginIcons
import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import gdscript.GdIcon
import gdscript.psi.GdClassVarDeclTl
import gdscript.psi.GdTypes

/**
 * Offers getter/setter method names - which you can then generate
 */
class GetterSetterNameCompletion : CompletionContributor() {

    val SET_METHOD = psiElement().withParent(psiElement(GdTypes.SET_METHOD_ID_NM))
    val GET_METHOD = psiElement().withParent(psiElement(GdTypes.GET_METHOD_ID_NM))

    override fun fillCompletionVariants(parameters: CompletionParameters, result: CompletionResultSet) {
        if (SET_METHOD.accepts(parameters.position)) {
            addMethodName("set", parameters.position, result)
        } else if (GET_METHOD.accepts(parameters.position)) {
            addMethodName("get", parameters.position, result)
        }
    }

    private fun addMethodName(prefix: String, element: PsiElement, result: CompletionResultSet) {
        val name = PsiTreeUtil.getStubOrPsiParentOfType(element, GdClassVarDeclTl::class.java)?.name ?: return
        result.addElement(
            GdLookup.create(
                "_${prefix}_${name.trimStart('_')}",
                priority = GdLookup.TOP,
                icon = GdScriptPluginIcons.GDScriptIcons.METHOD_MARKER,
            )
        )
    }

}
