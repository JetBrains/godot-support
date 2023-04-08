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
import gdscript.psi.GdClassNaming
import gdscript.psi.GdFuncDeclEx
import gdscript.psi.GdMethodDeclTl
import gdscript.psi.GdParamList
import gdscript.psi.GdRefIdNm
import gdscript.psi.GdTypes
import gdscript.psi.GdVarDeclSt
import gdscript.psi.utils.GdClassMemberUtil
import gdscript.psi.utils.GdClassMemberUtil.constructors
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
        val paramLists = mutableListOf<GdParamList?>()
        val paramDefinitions = when (declaration) {
            is GdMethodDeclTl -> {
                if (declaration.isVariadic) return
                if (declaration.name == "emit") {
                    val signal = PsiGdSignalUtil.getDeclaration(call) ?: return
                    paramLists.add(signal.paramList)
                    description = signal.text
                    arrayOf(signal.parameters)
                } else {
                    description = declaration.methodHeader()
                    paramLists.add(declaration.paramList)
                    arrayOf(declaration.parameters)
                }
            }

            is GdVarDeclSt -> {
                val lambda = if (declaration.expr is GdFuncDeclEx) declaration.expr as GdFuncDeclEx else null ?: return
                paramLists.add(lambda.paramList)
                arrayOf(lambda.parameters)
            }

            is GdClassNaming -> {
                description = declaration.classname
                GdClassMemberUtil
                    .listClassMemberDeclarations(declaration, constructors = true)
                    .constructors()
                    .map {
                        paramLists.add(it.paramList)
                        it.parameters
                    }
                    .toTypedArray()
            }

            else -> null
        } ?: return

        var minSize = 99
        var maxSize = 0
        paramDefinitions.forEach { params ->
            minSize = minOf(minSize, params.size)
            maxSize = maxOf(maxSize, params.size)
        }

        // Check number of arguments
        if (index >= maxSize) {
            val toRemote = mutableListOf<PsiElement>(element)
            if (index > 0) toRemote.addIfNotNull(element.prevNonWhiteCommentToken())
            holder
                .newAnnotation(HighlightSeverity.ERROR, "Too many arguments for $description")
                .range(element.textRange)
                .withFix(GdRemoveElementsAction(*toRemote.toTypedArray()))
                .create()
            return
        } else if (index < minSize) {
            // TODO requires to exclude defaults
//            holder
//                .newAnnotation(HighlightSeverity.ERROR, "Not enough arguments for $description")
//                .range(element.textRange)
//                .create()
//            return
        }

        // Check argument's type
        var paramType = ""
        var paramName = ""
        var currentType = ""
        val matched = paramDefinitions.any { params ->
            if (index >= params.size) return@any false

            paramType = params.values.toTypedArray()[index]
            paramName = params.keys.toTypedArray()[index]
            currentType = element.returnType

            GdExprUtil.typeAccepts(currentType, paramType, element.project)
        }

        if (!matched) {
            val annotation = holder
                .newAnnotation(HighlightSeverity.ERROR, "")

            if (paramLists.size == 1) {
                annotation
                    .tooltip("<html><body>Type mismatch for $paramName<table><tr><td>Required:</td><td>$paramType</td></tr><tr><td>Found:</td><td>$currentType</td></tr></table></html></body>")
                val typedVal = paramLists.first()!!.paramList[index]?.typed?.typedVal
                if (typedVal != null) annotation.withFix(GdChangeTypeFix(typedVal, currentType))
            } else {
                annotation.tooltip("None of function overrides matches param of type $currentType")
            }

            annotation.range(element.textRange)
                .create()
        }
    }

}
