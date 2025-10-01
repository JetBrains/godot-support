package gdscript.codeInsight

import com.intellij.codeInsight.CodeInsightSettings
import com.intellij.lang.parameterInfo.*
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.*
import gdscript.psi.utils.GdClassMemberUtil
import gdscript.utils.GdAnnotationUtil

class GdParameterInfoHandler : ParameterInfoHandler<PsiElement, PsiElement>, DumbAware {

    override fun findElementForParameterInfo(context: CreateParameterInfoContext): PsiElement? {
        val element = getFunctionCall(context)
        if (element != null) {
            val refId = PsiTreeUtil.findChildrenOfType(element.expr, GdRefIdRef::class.java).lastOrNull() ?: return null
            when (val declaration = GdClassMemberUtil.findDeclaration(refId)) {
                is GdMethodDeclTl -> {
                    context.itemsToShow = arrayOf(declaration)
                }

                is GdVarDeclSt ->
                    if (declaration.expr is GdFuncDeclEx) {
                        context.itemsToShow = arrayOf(declaration.expr as GdFuncDeclEx)
                    }

                is GdClassNaming -> {
                    val methods =
                        PsiTreeUtil.getStubChildrenOfTypeAsList(declaration.containingFile, GdMethodDeclTl::class.java)
                    context.itemsToShow = methods.filter {
                        it.isConstructor
                    }.toTypedArray()
                }
            }

            return element
        }

        val annotation = getAnnotation(context) ?: return null
        context.itemsToShow = arrayOf(annotation)

        return annotation
    }

    override fun findElementForUpdatingParameterInfo(context: UpdateParameterInfoContext): PsiElement? {
        val element: PsiElement = getFunctionCall(context) ?: getAnnotation(context) ?: return null
        val offset = context.offset
        val elRange: TextRange = element.textRange
        val index =
            if (offset <= elRange.startOffset || offset >= elRange.endOffset) -1
            else {
                val argumentNode = when (element) {
                    is GdCallEx -> element.argList?.node ?: element.node
                    is GdAnnotationTl -> element.annotationParams?.node ?: element.node
                    else -> null
                } ?: return null

                ParameterInfoUtils.getCurrentParameterIndex(argumentNode, offset, GdTypes.COMMA)
            }
        context.setCurrentParameter(index)

        return element
    }

    override fun updateUI(declaration: PsiElement?, context: ParameterInfoUIContext) {
        val currentParam = context.currentParameterIndex
        var startOffset = -1
        var endOffset = -1
        val fullSignature = CodeInsightSettings.getInstance().SHOW_FULL_SIGNATURES_IN_PARAMETER_INFO

        var isVariadic = false
        val builder = StringBuilder()
        var ending = ""

        val parameters = when(declaration) {
            is GdMethodDeclTl -> {
                if (fullSignature) {
                    builder.append("func ${declaration.name}(")
                    ending = ")"
                    if (declaration.returnType.isNotBlank()) ending += " -> ${declaration.returnType}"
                }
                isVariadic = declaration.isVariadic
                declaration.parameters
            }
            is GdFuncDeclEx -> {
                if (fullSignature) {
                    builder.append("func ${declaration.funcDeclIdNmi?.text ?: ""}(")
                    ending = ")"
                    if (declaration.returnType.isNotBlank()) ending += " -> ${declaration.returnType}"
                }
                declaration.parameters
            }
            is GdAnnotationTl -> {
                val specification = GdAnnotationUtil.get(declaration)
                if (fullSignature) {
                    builder.append("${declaration.annotationType.text}(")
                    ending = ")"
                }
                isVariadic = specification?.variadic ?: false
                specification?.parameters ?: emptyMap<String, String>()
            }
            else -> emptyMap<String, String>()
        }

        var i = 0
        parameters.forEach {
            if (i > 0) {
                builder.append(", ")
            }
            val start = builder.length
            builder.append("${it.key}: ${it.value}")
            if (currentParam == i) {
                startOffset = start
                endOffset = builder.length
            }
            i += 1
        }

        if (i <= 0) {
            if (isVariadic) {
                repeat(currentParam) {
                    builder.append("$it, ")
                }
                builder.append("vararg")
            } else {
                builder.append("no parameters")
            }
        }
        builder.append(ending)

        context.setupUIComponentPresentation(
            builder.toString(),
            startOffset,
            endOffset,
            false,
            false,
            false,
            context.defaultParameterColor,
        )
    }

    override fun updateParameterInfo(parameterOwner: PsiElement, context: UpdateParameterInfoContext) {
        context.parameterOwner = parameterOwner
    }

    override fun showParameterInfo(element: PsiElement, context: CreateParameterInfoContext) {
        context.showHint(element, element.textRange.startOffset + 1, this)
    }

    private fun getFunctionCall(context: ParameterInfoContext): GdCallEx? {
        val element = context.file.findElementAt(context.offset) ?: return null
        return PsiTreeUtil.getParentOfType(element, GdCallEx::class.java)
    }

    private fun getAnnotation(context: ParameterInfoContext): GdAnnotationTl? {
        val element = context.file.findElementAt(context.offset) ?: return null
        return PsiTreeUtil.getParentOfType(element, GdAnnotationTl::class.java)
    }

}
