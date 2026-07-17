package gdscript.codeInsight

import com.intellij.codeInsight.hints.HintInfo
import com.intellij.codeInsight.hints.HintInfo.MethodInfo
import com.intellij.codeInsight.hints.InlayInfo
import com.intellij.codeInsight.hints.InlayParameterHintsProvider
import com.intellij.polySymbols.PolySymbol
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.refactoring.suggested.startOffset
import com.intellij.util.containers.toArray
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.polySymbols.gdDeclaringClassName
import gdscript.polySymbols.gdPsiSourceElement
import gdscript.polySymbols.gdSignature
import gdscript.polySymbols.resolve.GdSymbolResolverUtil.resolveSymbolReferences
import gdscript.psi.GdAnnotationTl
import gdscript.psi.GdCallEx
import gdscript.psi.GdFuncDeclEx
import gdscript.psi.GdRefIdRef
import gdscript.psi.GdVarDeclSt
import gdscript.psi.utils.GdExprUtil
import gdscript.psi.utils.PsiGdSignalUtil
import gdscript.utils.GdAnnotationUtil

class GdInlayParameterHintProvider : InlayParameterHintsProvider {

    override fun getHintInfo(element: PsiElement): HintInfo? {
        if (element is GdCallEx) {
            val id = PsiTreeUtil.findChildrenOfType(element.expr, GdRefIdRef::class.java).lastOrNull() ?: return null
            val symbols = id.resolveSymbolReferences()

            val constructors = symbols.filter { it.kind == GdPolySymbolKind.CONSTRUCTOR }
            if (constructors.isNotEmpty()) return constructorHintInfo(element, constructors)

            val method = symbols.firstOrNull { it.kind == GdPolySymbolKind.METHOD }
            if (method != null) {
                if (method.name == "emit") {
                    val signal = PsiGdSignalUtil.getDeclaration(element)
                    if (signal != null) return MethodInfo(method.name, signal.parameters.keys.toList())
                }
                val signature = method.gdSignature ?: return null
                return MethodInfo(method.name, signature.parameters.map { it.name })
            }

            // Lambdas - inherently PSI-only, no SDK counterpart is possible.
            val declaration = symbols.firstOrNull()?.gdPsiSourceElement?.parent as? GdVarDeclSt ?: return null
            val lambda = declaration.expr as? GdFuncDeclEx ?: return null
            return MethodInfo(lambda.funcDeclIdNmi?.text.orEmpty(), lambda.parameters.keys.toList())
        } else if (element is GdAnnotationTl) {
            val definition = GdAnnotationUtil.get(element) ?: return null
            return MethodInfo(element.annotationType.text, definition.parameters.keys.toList())
        }

        return null
    }

    private fun constructorHintInfo(element: GdCallEx, constructors: List<PolySymbol>): MethodInfo? {
        val currentParams = element.argList?.argExprList ?: return null
        val constructor = constructors.find { ctor ->
            val signature = ctor.gdSignature ?: return@find false
            signature.parameters.size == currentParams.size &&
                currentParams.withIndex().all { (i, param) -> GdExprUtil.typeAccepts(param.returnType, signature.parameters[i].type, element) }
        } ?: return null
        val signature = constructor.gdSignature ?: return null
        return MethodInfo(constructor.gdDeclaringClassName.orEmpty(), signature.parameters.map { it.name })
    }

    override fun getParameterHints(element: PsiElement): List<InlayInfo> {
        if (element is GdCallEx) {
            val id = PsiTreeUtil.findChildrenOfType(element.expr, GdRefIdRef::class.java).lastOrNull() ?: return emptyList()
            val symbols = id.resolveSymbolReferences()

            val constructors = symbols.filter { it.kind == GdPolySymbolKind.CONSTRUCTOR }
            val method = symbols.firstOrNull { it.kind == GdPolySymbolKind.METHOD }
            val lambdaDeclaration = symbols.firstOrNull()?.gdPsiSourceElement?.parent as? GdVarDeclSt

            var params: Array<String> = emptyArray()
            var isVariadic = false

            if (constructors.isNotEmpty()) {
                val usedParams = element.argList?.argExprList
                for (ctor in constructors) {
                    val signature = ctor.gdSignature ?: continue
                    val hints = signature.parameters
                    if (usedParams == null || hints.size != usedParams.size) continue
                    var ok = true
                    for (i in hints.indices) {
                        val t1 = usedParams[i].expr.returnType
                        val t2 = hints[i].type
                        ok = ok && GdExprUtil.typeAccepts(t1, t2, element)
                    }

                    if (ok) {
                        params = hints.map { it.name }.toTypedArray()
                        break
                    }
                }
            } else if (method != null) {
                val signature = method.gdSignature
                if (signature != null) {
                    params = signature.parameters.map { it.name }.toTypedArray()
                    isVariadic = signature.isVariadic

                    if (method.name == "emit") {
                        val signal = PsiGdSignalUtil.getDeclaration(element)
                        if (signal != null) {
                            params = signal.parameters.keys.toArray(emptyArray())
                        }
                    }
                }
            } else if (lambdaDeclaration != null && lambdaDeclaration.expr is GdFuncDeclEx) {
                val lambda = lambdaDeclaration.expr as GdFuncDeclEx
                params = lambda.parameters.keys.toArray(emptyArray())
            }
            if (params.isEmpty()) return emptyList()

            val args = element.argList?.argExprList ?: return emptyList()

            return args.mapIndexedNotNull { i, arg ->
                if (isVariadic && i >= params.size - 1) return@mapIndexedNotNull null
                val paramName = params.getOrNull(i) ?: return@mapIndexedNotNull null
                InlayInfo(paramName, arg.startOffset, paramName == arg.expr.text)
            }
        } else if (element is GdAnnotationTl) {
            val definition = GdAnnotationUtil.get(element)?.parameters ?: return emptyList()
            val keys = definition.keys.toTypedArray()

            return element.annotationParams?.exprList?.mapIndexedNotNull { index, it ->
                if (index < keys.size) {
                    InlayInfo(keys[index], it.startOffset, keys[index] == it.text)
                } else null
            } ?: emptyList()
        }

        return emptyList()
    }

    override fun getDefaultBlackList(): MutableSet<String> {
        return mutableSetOf()
    }

}
