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
import gdscript.psi.utils.PsiGdNamedUtil

/**
 * Checks matching return types
 * TODO ii
 */
class GdReturnTypeAnnotator : Annotator {

    val NUMBER_MIXED = arrayOf(GdKeywords.INT, GdKeywords.FLOAT);

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element is GdReturnHintVal) {
            checkParentType(element, holder);
        } else if (element is GdReturnStmt) {
            checkStmtType(element, holder);
        }
    }

    private fun checkParentType(element: GdReturnHintVal, holder: AnnotationHolder) {
        val returnType = element.text;
        val methodDecl = getMethodDecl(element) ?: return;
        if (methodDecl is GdFuncDeclEx) return;
        val method = (methodDecl as GdMethodDeclTl).methodIdNmi ?: return;

        val parentMethod = PsiGdNamedUtil.findInParent(method, variables = false, withLocalScopes = true);
        if (parentMethod !== null && parentMethod is GdMethodDeclTl) {
            val parentReturnType = parentMethod.returnType;
            if (parentReturnType != "" && returnType != parentReturnType && !extraAllowed(parentReturnType, returnType)) {
                holder
                    .newAnnotation(HighlightSeverity.ERROR,
                        "Return type [$returnType] does not match parent's [$parentReturnType]")
                    .range(element.textRange)
                    .withFix(GdChangeReturnTypeFix(element, parentReturnType))
                    .create()
            }
        }
    }

    private fun checkStmtType(element: GdReturnStmt, holder: AnnotationHolder) {
        val method = getMethodDecl(element) ?: return;
        val returnType = when (method) {
            is GdMethodDeclTl -> method.returnType;
            is GdFuncDeclEx -> method.returnType;
            else -> "";
        }

        val myType = element.expr?.returnType ?: GdKeywords.VOID;
        if (myType.isEmpty()) {
            return;
        }

        if (returnType.isNotEmpty()) {
            if (myType != returnType
                && !extraAllowed(myType, returnType)
                && !PsiGdNamedUtil.hasParent(myType, returnType, element.project)
            ) {
                val hint = when (method) {
                    is GdMethodDeclTl -> method.returnHint?.returnHintVal;
                    is GdFuncDeclEx -> method.returnHint?.returnHintVal;
                    else -> null;
                }
                holder
                    .newAnnotation(HighlightSeverity.ERROR, "Returns a type [$myType] which do not match function's [$returnType]")
                    .withFix(GdChangeReturnTypeFix(hint!!, myType))
                    .range(element.textRange)
                    .create()
            }
        } else {
            var matched = true;
            PsiTreeUtil.findChildrenOfType(method, GdReturnStmt::class.java).forEach {
                val type = it.expr?.returnType ?: GdKeywords.VOID;
                matched = matched && type == myType;
            }
            if (matched) {
                holder
                    .newAnnotation(HighlightSeverity.WEAK_WARNING, "Function's return type can be specified as [$myType]")
                    .range(element.textRange)
                    .withFix(GdAddReturnType(method, myType))
                    .create()
            }
        }
    }

    private fun extraAllowed(type1: String, type2: String): Boolean {
        return (NUMBER_MIXED.contains(type1) && NUMBER_MIXED.contains(type2))
                || (type1 == "Array" && type2.startsWith("Array"))
                || (type2 == "Array" && type1.startsWith("Array"));
    }

    private fun getMethodDecl(element: PsiElement): PsiElement? {
        return PsiTreeUtil.findFirstParent(element) {
            it is GdMethodDeclTl || it is GdFuncDeclEx
        };
    }

}
