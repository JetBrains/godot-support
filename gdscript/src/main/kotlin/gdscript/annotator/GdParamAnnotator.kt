package gdscript.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import gdscript.action.quickFix.GdChangeTypeFix
import gdscript.action.quickFix.GdRemoveElementsAction
import gdscript.completion.utils.GdMethodCompletionUtil.shortMethodHeader
import gdscript.psi.GdCallEx
import gdscript.psi.GdClassNaming
import gdscript.psi.GdFuncDeclEx
import gdscript.psi.GdMethodDeclTl
import gdscript.psi.GdRefIdNm
import gdscript.psi.GdVarDeclSt
import gdscript.psi.utils.GdClassMemberUtil
import gdscript.psi.utils.GdClassMemberUtil.constructors
import gdscript.psi.utils.GdExprUtil
import gdscript.psi.utils.PsiGdSignalUtil
import gdscript.reference.GdClassMemberReference
import gdscript.utils.PsiElementUtil.nextNonWhiteCommentToken
import gdscript.utils.PsiElementUtil.prevNonWhiteCommentToken
import gdscript.utils.StringUtil.isDynamicType

class GdParamAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element !is GdCallEx) return

        var minSize = 99
        var maxSize = 0

        val refId = PsiTreeUtil.findChildOfType(element, GdRefIdNm::class.java) ?: return
        val declaration = GdClassMemberReference(refId).resolveDeclaration() ?: return
        val descriptions = mutableListOf<String>()

        val paramLists = when (declaration) {
            is GdMethodDeclTl -> {
                if (declaration.isVariadic) return
                if (declaration.name == "emit") {
                    val signal = PsiGdSignalUtil.getDeclaration(element) ?: return
                    descriptions.add(declaration.shortMethodHeader())
                    arrayOf(signal.paramList?.paramList)
                } else {
                    descriptions.add(declaration.shortMethodHeader())
                    arrayOf(declaration.paramList?.paramList)
                }
            }

            is GdVarDeclSt -> {
                val lambda =
                    if (declaration.expr is GdFuncDeclEx) declaration.expr as GdFuncDeclEx else null ?: return
                descriptions.add(lambda.shortMethodHeader())
                arrayOf(lambda.paramList?.paramList)
            }

            is GdClassNaming -> {
                GdClassMemberUtil
                    .listClassMemberDeclarations(declaration, constructors = true)
                    .constructors()
                    .map {
                        descriptions.add(it.shortMethodHeader())
                        it.paramList?.paramList
                    }
                    .toTypedArray()
            }

            else -> null
        }?.mapNotNull { it } ?: return

        if (descriptions.size > paramLists.size) minSize = 0 // filtered by mapNotNull of empty constructor

        val paramTypes: HashMap<Int, MutableList<String>> = hashMapOf()
        paramLists.forEachIndexed { index, params ->
            minSize = minOf(minSize, params.size)
            maxSize = maxOf(maxSize, params.size)
            for (i in 0 until params.size) {
                if (params[i].expr != null) {
                    minSize = minOf(minSize, i)
                    break
                }
            }

            paramTypes[index] = mutableListOf()
            params.forEach { param ->
                paramTypes[index]!!.add(param.returnType)
            }
        }

        val usedParamSize = element.argList?.argExprList?.size ?: 0

        // Check number of arguments
        if (usedParamSize > maxSize && element.argList != null) {
            val toRemoveList = mutableListOf<PsiElement>()
            var toRemove: PsiElement? = element.argList!!.argExprList[maxSize]
            if (maxSize > 0) toRemoveList.add(toRemove!!.prevNonWhiteCommentToken()!!)

            while (toRemove != null) {
                toRemoveList.add(toRemove)
                toRemove = toRemove.nextNonWhiteCommentToken()
            }

            holder
                .newAnnotationGd(element.project, HighlightSeverity.ERROR, "Too many arguments")
                .range(element.textRange)
                .withFix(GdRemoveElementsAction(*toRemoveList.toTypedArray()))
                .create()
            return
        } else if (minSize in 1..98 && usedParamSize < minSize) {
            holder
                .newAnnotationGd(element.project, HighlightSeverity.ERROR, "Not enough arguments")
                .range(element.textRange)
                .create()
            return
        }

        if (usedParamSize == 0) return

        // Check arguments types
        val actualTypes = element.argList?.argExprList?.map { it.returnType }?.toTypedArray() ?: emptyArray()

        val matched = paramTypes.values
            .filter { it.size == usedParamSize }
            .map { definedParams ->
                definedParams.mapIndexed { pIndex, definedParam ->
                    GdExprUtil.typeAccepts(actualTypes[pIndex], definedParam, element)
                }
            }

        if (matched.isEmpty()) return

        // One of overrides matched all params
        if (matched.any { it.all { p -> p } }) return

        if (paramLists.size > 1) {
            holder
                .newAnnotationGd(element.project, HighlightSeverity.ERROR, "")
                .tooltip("""<html><body>
                    None of method definitions can be called with supplied arguments
                    <ul>
                        ${descriptions.joinToString("") { "<li><strong>$it</strong></li>" }}
                    </ul>
                    </body></html>""".trimIndent())
                .range(element.textRange)
                .create()
            return
        } else {
            val params = paramLists.first()
            matched.first().forEachIndexed { pIndex, ok ->
                if (!ok) {
                    val param = params[pIndex] ?: return@forEachIndexed
                    val actualParam = element.argList?.argExprList?.getOrNull(pIndex) ?: return@forEachIndexed
                    val actualType = actualTypes[pIndex]

                    val annotator = holder
                        .newAnnotationGd(element.project, HighlightSeverity.ERROR, "")
                        .tooltip("""
                            <html><body>
                                Type mismatch for ${param.varNmi.name}
                                <table>
                                    <tr>
                                        <td>Required:</td>
                                        <td>${param.returnType}</td>
                                    </tr>
                                    <tr>
                                        <td>Found:</td>
                                        <td>${actualType}</td>
                                    </tr>
                                </table>
                            </html></body>""".trimIndent())
                        .range(actualParam.textRange)
                    if (!actualType.isDynamicType() && param.typed != null) {
                        annotator.withFix(GdChangeTypeFix(param.typed!!.typedVal, actualType))
                    }
                    annotator.create()
                }
            }
            return
        }
    }

}
