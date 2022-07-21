package gdscript.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import gdscript.GdKeywords
import gdscript.action.quickFix.GdAddReturnType
import gdscript.action.quickFix.GdChangeReturnType
import gdscript.psi.GdMethodDeclTl
import gdscript.psi.GdReturnHintVal
import gdscript.psi.GdReturnStmt
import gdscript.psi.utils.PsiGdNamedUtil

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
        val method = PsiTreeUtil.getStubOrPsiParentOfType(element, GdMethodDeclTl::class.java)?.methodIdNmi ?: return;

        val parentMethod = PsiGdNamedUtil.findInParent(method, variable = false, constant = false, enum = false);
        if (parentMethod !== null && parentMethod is GdMethodDeclTl) {
            val parentReturnType = parentMethod.returnType;
            if (parentReturnType != "" && returnType != parentReturnType && !extraAllowed(parentReturnType, returnType)) {
                holder
                    .newAnnotation(HighlightSeverity.ERROR,
                        "Return type [$returnType] does not match parent's [$parentReturnType]")
                    .range(element.textRange)
                    .withFix(GdChangeReturnType(element, parentReturnType))
                    .create()
            }
        }
    }

    private fun checkStmtType(element: GdReturnStmt, holder: AnnotationHolder) {
        val method = PsiTreeUtil.getStubOrPsiParentOfType(element, GdMethodDeclTl::class.java) ?: return;
        val returnType = method.returnType;
        val myType = element.expr?.returnType ?: GdKeywords.VOID;
        if (myType.isEmpty()) {
            return;
        }

        if (returnType.isNotEmpty()) {

            if (myType != returnType && !extraAllowed(myType, returnType)) {
                holder
                    .newAnnotation(HighlightSeverity.ERROR, "Returns a type [$myType] which does not match function's [$returnType]")
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

}
