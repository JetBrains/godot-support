package gdscript.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.elementType
import com.intellij.util.containers.addIfNotNull
import gdscript.action.quickFix.GdRemoveElementsAction
import gdscript.completion.utils.GdMethodCompletionUtil.methodHeader
import gdscript.psi.GdArgExpr
import gdscript.psi.GdCallEx
import gdscript.psi.GdFuncDeclEx
import gdscript.psi.GdMethodDeclTl
import gdscript.psi.GdRefIdNm
import gdscript.psi.GdSignalDeclTl
import gdscript.psi.GdTypes
import gdscript.reference.GdClassMemberReference
import gdscript.utils.PsiElementUtil.prevNonWhiteCommentToken

class GdParamAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element !is GdArgExpr) return

        var index = 0
        var current = element.prevNonWhiteCommentToken(GdTypes.COMMA)
        while (current.elementType == GdTypes.ARG_EXPR) {
            current = current?.prevNonWhiteCommentToken(GdTypes.COMMA)
            index++
        }

        val call = PsiTreeUtil.getParentOfType(element, GdCallEx::class.java) ?: return
        val refId = PsiTreeUtil.findChildOfType(call.expr, GdRefIdNm::class.java) ?: return
        val declaration = GdClassMemberReference(refId).resolveDeclaration() ?: return

        var description = ""
        val params = when (declaration) {
            is GdMethodDeclTl -> {
                description = declaration.methodHeader()
                declaration.paramList
            }
            is GdSignalDeclTl -> {
                description = ""
                declaration.paramList
            }
            is GdFuncDeclEx -> {

                description = ""
                declaration.paramList
            }
            else -> null
        }?.paramList ?: return

        // Check number of arguments
        if (params.size <= index) {
            val toRemote = mutableListOf<PsiElement>(element)
            if (index > 0) toRemote.addIfNotNull(element.prevNonWhiteCommentToken())
            holder
                .newAnnotation(HighlightSeverity.ERROR, "Too many argument for $description")
                .range(element.textRange)
                .withFix(GdRemoveElementsAction(*toRemote.toTypedArray()))
                .create()
            return
        }

        // Check argument's type
        
    }

}
