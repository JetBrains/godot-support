package gdscript.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import gdscript.GdKeywords
import gdscript.action.quickFix.GdAddReturnType
import gdscript.action.quickFix.GdChangeReturnTypeFix
import gdscript.psi.*
import gdscript.psi.utils.GdClassMemberUtil
import gdscript.psi.utils.GdExprUtil

/**
 * Checks matching return types of method, and it's parent method
 * and existence in method declaration
 */
class GdReturnTypeAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        when (element) {
            is GdReturnHintVal -> checkParentType(element, holder);
            is GdFlowSt -> checkStmtType(element, holder);
        }
    }

    private fun checkParentType(element: GdReturnHintVal, holder: AnnotationHolder) {
        if (!isMethod(element)) return
        val returnType = element.text
        val declaration = PsiTreeUtil.getStubOrPsiParentOfType(element, GdMethodDeclTl::class.java) ?: return
        val id = declaration.methodIdNmi ?: return
        val parent = GdClassMemberUtil.findDeclaration(id) ?: return

        val parentReturnType = when (parent) {
            is GdMethodDeclTl -> parent.returnType
            else -> return
        }

        if (GdExprUtil.typeAccepts(returnType, parentReturnType, element)) return
        holder
            .newAnnotation(
                HighlightSeverity.ERROR,
                "Return type [$returnType] does not match parent's [$parentReturnType]"
            )
            .range(element.textRange)
            .withFix(GdChangeReturnTypeFix(element, parentReturnType))
            .create()
    }

    private fun checkStmtType(element: GdFlowSt, holder: AnnotationHolder) {
        if (element.type != GdKeywords.FLOW_RETURN) return;
        val method = getMethodDecl(element) ?: return;
        val hint: GdReturnHintVal?;
        val returnType = when (method) {
            is GdMethodDeclTl -> {
                hint = method.returnHint?.returnHintVal; method.returnType
            };
            is GdFuncDeclEx -> {
                hint = method.returnHint?.returnHintVal; method.returnType
            };
            else -> return;
        }

        val stmtType = element.expr?.returnType ?: GdKeywords.VOID;
        if (returnType.isNotEmpty()) {
            if (GdExprUtil.typeAccepts(stmtType, returnType, element)) return;
            holder
                .newAnnotation(
                    HighlightSeverity.ERROR,
                    "Returns a type [$stmtType] which do not match function's [$returnType]"
                )
                .withFix(GdChangeReturnTypeFix(hint!!, stmtType))
                .range(element.textRange)
                .create()
        } else {
            var matched = true
            PsiTreeUtil.findChildrenOfType(method, GdFlowSt::class.java).forEach {
                if (it.type == GdKeywords.FLOW_RETURN) {
                    val type = it.expr?.returnType ?: GdKeywords.VOID
                    matched = matched && GdExprUtil.typeAccepts(type, stmtType, element);
                }
            }
            if (matched && stmtType.isNotBlank()) {
                holder
                    .newAnnotation(HighlightSeverity.WEAK_WARNING, "Function's return type can be specified as [$stmtType]")
                    .range(element.textRange)
                    .withFix(GdAddReturnType(method, stmtType))
                    .create()
            }
        }
    }

    private fun getMethodDecl(element: PsiElement): PsiElement? {
        return PsiTreeUtil.findFirstParent(element) {
            it is GdMethodDeclTl || it is GdFuncDeclEx
        };
    }

    private fun isMethod(element: PsiElement): Boolean {
        return getMethodDecl(element) is GdMethodDeclTl;
    }

}
