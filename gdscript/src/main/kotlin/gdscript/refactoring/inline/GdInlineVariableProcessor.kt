package gdscript.refactoring.inline

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.search.searches.ReferencesSearch
import com.intellij.refactoring.BaseRefactoringProcessor
import com.intellij.usageView.BaseUsageViewDescriptor
import com.intellij.usageView.UsageInfo
import com.intellij.usageView.UsageViewDescriptor
import com.intellij.util.IncorrectOperationException
import com.intellij.openapi.diagnostic.Logger
import gdscript.psi.*

class GdInlineVariableProcessor : BaseRefactoringProcessor {

    private val element: PsiElement
    private val initializer: PsiElement?
    private val inlineThisOnly: Boolean
    private val removeDeclaration: Boolean

    companion object {
        private val LOG = Logger.getInstance(GdInlineVariableProcessor::class.java)
    }

    constructor(
        project: Project,
        element: PsiElement,
        initializer: PsiElement?,
        inlineThisOnly: Boolean,
        removeDeclaration: Boolean,
    ) : super(project) {
        this.element = element
        this.initializer = initializer
        this.inlineThisOnly = inlineThisOnly
        this.removeDeclaration = removeDeclaration
    }

    override fun createUsageViewDescriptor(usages: Array<out UsageInfo>): UsageViewDescriptor {
        return BaseUsageViewDescriptor(element)
    }

    override fun findUsages(): Array<UsageInfo> {
        val usages = mutableListOf<UsageInfo>()
        ReferencesSearch.search(element).forEach {
            usages.add(UsageInfo(it))
        }

        return usages.toTypedArray()
    }

    override fun performRefactoring(usages: Array<out UsageInfo>) {
        val parent = element.parent
        val replacementExpr: GdExpr = when (parent) {
            is GdVarDeclSt -> parent.expr
            is GdClassVarDeclTl -> parent.expr
            is GdConstDeclSt -> parent.expr
            is GdConstDeclTl -> parent.expr
            else -> null
        } ?: return

        if (inlineThisOnly) {
            this.initializer?.replace(replacementExpr)
        } else {
            usages.forEach {
                val element = it.element ?: return@forEach
                try {
                    element.replace(replacementExpr)
                } catch (e: IncorrectOperationException) {
                    LOG.error(e)
                }
            }
        }

        if (!inlineThisOnly && removeDeclaration) {
            try {
                parent.delete()
            } catch (e: IncorrectOperationException) {
                LOG.error(e)
            }
        }
    }

    override fun getCommandName(): String {
        return "Inline Variable"
    }
}
