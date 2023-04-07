package gdscript.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.elementType
import com.intellij.util.containers.addIfNotNull
import gdscript.action.quickFix.GdChangeTypeFix
import gdscript.action.quickFix.GdRemoveElementsAction
import gdscript.completion.utils.GdMethodCompletionUtil.methodHeader
import gdscript.psi.GdArgExpr
import gdscript.psi.GdCallEx
import gdscript.psi.GdFuncDeclEx
import gdscript.psi.GdMethodDeclTl
import gdscript.psi.GdParamList
import gdscript.psi.GdRefIdNm
import gdscript.psi.GdTypes
import gdscript.psi.GdVarDeclSt
import gdscript.psi.utils.GdExprUtil
import gdscript.psi.utils.PsiGdSignalUtil
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
        var paramList: GdParamList? = null
        val params = when (declaration) {
            is GdMethodDeclTl -> {
                if (declaration.isVariadic) return
                if (declaration.name == "emit") {
                    val signal = PsiGdSignalUtil.getDeclaration(call) ?: return
                    paramList = signal.paramList
                    description = signal.text
                    signal.parameters
                } else {
                    description = declaration.methodHeader()
                    paramList = declaration.paramList
                    declaration.parameters
                }
            }

            is GdVarDeclSt -> {
                val lambda = if (declaration.expr is GdFuncDeclEx) declaration.expr as GdFuncDeclEx else null ?: return
                paramList = lambda.paramList
                lambda.parameters
            }

            else -> null
        } ?: return

        // Check number of arguments
        if (params.size <= index) {
            val toRemote = mutableListOf<PsiElement>(element)
            if (index > 0) toRemote.addIfNotNull(element.prevNonWhiteCommentToken())
            holder
                .newAnnotation(HighlightSeverity.ERROR, "Too many arguments for $description")
                .range(element.textRange)
                .withFix(GdRemoveElementsAction(*toRemote.toTypedArray()))
                .create()
            return
        }

        // Check argument's type
        val paramType = params.values.toTypedArray()[index]!!
        val currentType = element.returnType

        if (!GdExprUtil.typeAccepts(currentType, paramType, element.project)) {
            val annotation = holder
                .newAnnotation(HighlightSeverity.ERROR, "")
                .tooltip("<html><body>Type mismatch<table><tr><td>Required:</td><td>$paramType</td></tr><tr><td>Found:</td><td>$currentType</td></tr></table></html></body>")

            val typedVal = paramList?.paramList?.get(index)?.typed?.typedVal
            if (typedVal != null) annotation.withFix(GdChangeTypeFix(typedVal, currentType))
            
            annotation.range(element.textRange)
                .create()
        }
    }

}
