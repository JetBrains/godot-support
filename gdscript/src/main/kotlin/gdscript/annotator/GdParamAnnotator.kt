package gdscript.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.util.text.HtmlBuilder
import com.intellij.openapi.util.text.HtmlChunk
import com.intellij.polySymbols.PolySymbol
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import gdscript.GdScriptBundle
import gdscript.action.quickFix.GdChangeTypeFix
import gdscript.action.quickFix.GdRemoveElementsAction
import gdscript.completion.utils.GdMethodCompletionUtil.shortMethodHeader
import gdscript.polySymbols.GdClassSymbol
import gdscript.polySymbols.GdParameterInfo
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.polySymbols.GdSignature
import gdscript.polySymbols.gdCompletionTailText
import gdscript.polySymbols.gdPsiSourceElement
import gdscript.polySymbols.gdSignature
import gdscript.polySymbols.resolve.GdSymbolResolverUtil
import gdscript.polySymbols.resolve.GdSymbolResolverUtil.resolveSymbolReference
import gdscript.psi.GdCallEx
import gdscript.psi.GdFuncDeclEx
import gdscript.psi.GdMethodDeclTl
import gdscript.psi.GdParam
import gdscript.psi.GdPsiUtils
import gdscript.psi.GdRefIdRef
import gdscript.psi.GdVarDeclSt
import gdscript.psi.utils.GdClassMemberUtil
import gdscript.psi.utils.GdExprUtil
import gdscript.psi.utils.PsiGdSignalUtil
import gdscript.utils.PsiElementUtil.nextNonWhiteCommentToken
import gdscript.utils.PsiElementUtil.prevNonWhiteCommentToken
import gdscript.utils.StringUtil.isDynamicType
import org.jetbrains.annotations.NonNls

class GdParamAnnotator : Annotator {

    /**
     * One candidate signature for the call being validated. [psiParams] is the real PSI [GdParam]
     * list, present only when this candidate is PSI-backed - used solely to offer [GdChangeTypeFix],
     * which edits real PSI text and has nothing to edit for an SDK-backed candidate.
     */
    private data class Candidate(val description: String, val signature: GdSignature, val psiParams: List<GdParam>?)

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element !is GdCallEx) return

        val refId = PsiTreeUtil.findChildrenOfType(element.expr, GdRefIdRef::class.java).lastOrNull() ?: return

        val candidates: List<Candidate> = if (refId.text == "new") {
            // Qualified constructor call. The "new" token's own-reference resolution never reaches
            // SDK constructors (GdPsiPolySymbolUtil.resolveConstructorSymbol hardcodes a PSI-only
            // "_init" name lookup) - bypass it entirely and resolve the qualifier's class symbol
            // directly, mirroring that function's own approach up to (but not including) that step.
            val qualifier = GdClassMemberUtil.calledUpon(refId) ?: return
            val typeName = GdPsiUtils.getReturnType(qualifier)
            if (typeName.isEmpty()) return
            val classSymbol = GdSymbolResolverUtil.resolveCanonicalClassSymbol(element.project, typeName, refId) ?: return
            constructorCandidates(classSymbol) ?: return
        } else {
            val symbol = refId.resolveSymbolReference() ?: return
            when (symbol.kind) {
                // Bare constructor call: ClassName(...). kind == CLASS guarantees this is
                // GdPsiClassSymbol/GdPsiResourceClassSymbol/GdSdkClassSymbol, which implement
                // GdClassSymbol identically - the one sanctioned shared-interface cast.
                GdPolySymbolKind.CLASS -> constructorCandidates(symbol as? GdClassSymbol ?: return) ?: return

                GdPolySymbolKind.METHOD -> {
                    val signature = symbol.gdSignature ?: return
                    if (signature.isVariadic) return
                    if (symbol.name == "emit") {
                        val signal = PsiGdSignalUtil.getDeclaration(element) ?: return
                        val signalParams = signal.paramList?.paramList ?: emptyList()
                        listOf(
                            Candidate(
                                "${symbol.name}${symbol.gdCompletionTailText.orEmpty()}",
                                GdSignature(signalParams.map { GdParameterInfo(it.varNmi.name, it.returnType, it.expr != null) }, false),
                                signalParams,
                            )
                        )
                    } else {
                        listOf(Candidate("${symbol.name}${symbol.gdCompletionTailText.orEmpty()}", signature, symbol.psiParams()))
                    }
                }

                // Local variable/parameter holding a lambda literal - inherently PSI-only, no SDK
                // counterpart is possible.
                else -> {
                    val declaration = symbol.gdPsiSourceElement?.parent as? GdVarDeclSt ?: return
                    val lambda = declaration.expr as? GdFuncDeclEx ?: return
                    val params = lambda.paramList?.paramList ?: emptyList()
                    listOf(
                        Candidate(
                            lambda.shortMethodHeader(),
                            GdSignature(params.map { GdParameterInfo(it.varNmi.name, it.returnType, it.expr != null) }, false),
                            params,
                        )
                    )
                }
            }
        }

        if (candidates.isEmpty()) return

        var minSize = 99
        var maxSize = 0
        val paramTypes: HashMap<Int, MutableList<String>> = hashMapOf()

        candidates.forEachIndexed { index, candidate ->
            val params = candidate.signature.parameters
            minSize = minOf(minSize, params.size)
            maxSize = maxOf(maxSize, params.size)
            val firstDefaultIndex = params.indexOfFirst { it.hasDefault }
            minSize = minOf(minSize, if (firstDefaultIndex == -1) params.size else firstDefaultIndex)
            paramTypes[index] = params.map { it.type }.toMutableList()
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
                .newAnnotationGd(HighlightSeverity.ERROR, GdScriptBundle.message("annotator.too.many.arguments"))
                .range(element.textRange)
                .withFix(GdRemoveElementsAction(*toRemoveList.toTypedArray()))
                .create()
            return
        } else if (minSize in 1..98 && usedParamSize < minSize) {
            holder
                .newAnnotationGd(HighlightSeverity.ERROR, GdScriptBundle.message("annotator.not.enough.arguments"))
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


        if (candidates.size > 1) {
            val tooltip = HtmlBuilder()
                .append(GdScriptBundle.message("annotator.no.overload.matches"))
                .br()
                .append(
                    HtmlChunk.ul().children(
                        candidates.map {
                            @NonNls val description: String = it.description
                            HtmlChunk.li().child(HtmlChunk.text(description).bold()) }
                    ))
                .wrapWithHtmlBody()
                .toString()

            holder
                .newAnnotationGd(HighlightSeverity.ERROR, "")
                .tooltip(tooltip)
                .range(element.textRange)
                .create()
            return
        } else {
            val params = candidates.first().signature.parameters
            val psiParams = candidates.first().psiParams
            matched.first().forEachIndexed { pIndex, ok ->
                if (!ok) {
                    val param = params.getOrNull(pIndex) ?: return@forEachIndexed
                    val actualParam = element.argList?.argExprList?.getOrNull(pIndex) ?: return@forEachIndexed
                    val actualType = actualTypes[pIndex]

                    val tooltip = HtmlBuilder()
                        .append(GdScriptBundle.message("annotator.type.mismatch.for.parameter", param.name)).br()
                        .append(
                            HtmlChunk.tag("table").children(
                                HtmlChunk.tag("tr").children(
                                    HtmlChunk.tag("td").addText(GdScriptBundle.message("annotator.required")),
                                    HtmlChunk.tag("td").addText(param.type)
                                ),
                                HtmlChunk.tag("tr").children(
                                    HtmlChunk.tag("td").addText(GdScriptBundle.message("annotator.found")),
                                    HtmlChunk.tag("td").addText(actualType)
                                )
                            )
                        )
                        .wrapWithHtmlBody()
                        .toString()

                    val annotator = holder
                        .newAnnotationGd(HighlightSeverity.ERROR, "")
                        .tooltip(tooltip)
                        .range(actualParam.textRange)
                    val psiParam = psiParams?.getOrNull(pIndex)
                    if (!actualType.isDynamicType() && psiParam?.typed != null) {
                        annotator.withFix(GdChangeTypeFix(psiParam.typed!!.typedVal, actualType))
                    }
                    annotator.create()
                }
            }
            return
        }
    }

    private fun constructorCandidates(classSymbol: GdClassSymbol): List<Candidate>? {
        val constructors = GdSymbolResolverUtil.listConstructorSymbols(classSymbol)
        if (constructors.isEmpty()) return null
        val signatures = constructors.map { it.gdSignature }
        if (signatures.any { it == null || it.isVariadic }) return null
        return constructors.mapIndexed { i, ctor ->
            Candidate("${classSymbol.declaringClassName}${ctor.gdCompletionTailText.orEmpty()}", signatures[i]!!, ctor.psiParams())
        }
    }

    private fun PolySymbol.psiParams(): List<GdParam>? =
        (gdPsiSourceElement?.parent as? GdMethodDeclTl)?.paramList?.paramList

}
