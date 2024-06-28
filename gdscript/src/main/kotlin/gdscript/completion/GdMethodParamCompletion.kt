package gdscript.completion

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.psi.util.PsiTreeUtil
import gdscript.index.impl.GdClassIdIndex
import gdscript.psi.*
import gdscript.psi.utils.GdClassUtil
import gdscript.reference.GdClassMemberReference
import gdscript.utils.StringUtil.camelToSnakeCase

class GdMethodParamCompletion : CompletionContributor() {

    val PARAM_VAR_NMI = psiElement(GdTypes.IDENTIFIER)
        .withParents(
            GdVarNmi::class.java,
            GdParam::class.java,
        )

    override fun fillCompletionVariants(parameters: CompletionParameters, result: CompletionResultSet) {
        if (PARAM_VAR_NMI.accepts(parameters.position)) {
            // Types
            GdClassIdIndex.INSTANCE.getAllKeys(parameters.position.project)
                .forEach {
                    result.addElement(
                        GdLookup.create(
                            "${it.camelToSnakeCase()}: $it",
                            priority = GdLookup.BUILT_IN,
                        )
                    )
                }

            // Params of other methods
            val owner = GdClassUtil.getOwningClassElement(parameters.position)
            PsiTreeUtil.getChildrenOfType(owner, GdMethodDeclTl::class.java)
                ?.forEach { method ->
                    method.paramList?.paramList?.forEach {
                        result.addElement(
                            GdLookup.create(
                                it.text,
                                priority = GdLookup.USER_DEFINED,
                            )
                        )
                    }
                }

            // Local undeclared variables
            val method = PsiTreeUtil.getParentOfType(parameters.position, GdMethodDeclTl::class.java, GdFuncDeclEx::class.java)
            val vars = PsiTreeUtil.collectElementsOfType(method, GdRefIdRef::class.java)
            vars.forEach {
                if (GdClassMemberReference(it).resolveDeclaration() == null) {
                    result.addElement(
                        GdLookup.create(
                            it.text,
                            priority = GdLookup.LOCAL_USER_DEFINED,
                        )
                    )
                }
            }
        }
    }

}
