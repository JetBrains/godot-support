package gdscript.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.childrenOfType
import com.intellij.psi.util.elementType
import com.intellij.psi.util.nextLeaf
import gdscript.GdKeywords
import gdscript.GdScriptBundle
import gdscript.highlighter.GdHighlighterColors
import gdscript.polySymbols.GdPolySymbol
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.polySymbols.GdPolySymbolModifier
import gdscript.polySymbols.resolve.GdSymbolResolverUtil.resolveSymbolReference
import gdscript.polySymbols.sdk.GdSdkPolySymbol
import gdscript.psi.GdClassDeclTl
import gdscript.psi.GdClassNaming
import gdscript.psi.GdClassVarDeclTl
import gdscript.psi.GdEnumDeclTl
import gdscript.psi.GdMethodDeclTl
import gdscript.psi.GdNodePath
import gdscript.psi.GdRefIdRef
import gdscript.psi.GdTypes
import gdscript.psi.utils.GdClassMemberUtil
import gdscript.reference.GdClassMemberReference
import gdscript.settings.GdProjectSettingsState
import gdscript.settings.GdProjectState
import gdscript.utils.PsiElementUtil.getCallExpr
import gdscript.utils.PsiFileUtil.isInSdk
import project.psi.util.ProjectAutoloadUtil

/**
 * Colors references
 * Checks for existence
 */
class GdRefIdAnnotator : Annotator {

    private val objectContinuation = setOf(GdTypes.LRBR, GdTypes.LSBR, GdTypes.DOT)
    private val unresolvedTolerantTypes = setOf(GdKeywords.VARIANT, "Node", "Resource", "null")

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        val state = GdProjectSettingsState.getInstance(element).state.annotators
        if (element !is GdRefIdRef) return
        val txt = element.text

        if (txt == GdKeywords.SELF || txt == GdKeywords.SUPER) return
        if (GdKeywords.MATH_CONSTANTS.contains(txt)) {
            holder
                .newSilentAnnotation(HighlightSeverity.INFORMATION)
                .range(element.textRange)
                .textAttributes(GdHighlighterColors.KEYWORD)
                .create()
            return
        }

        var attribute = GdHighlighterColors.METHOD_CALL
        val reference = element.references.firstOrNull()
        if (reference?.isSoft == false && reference is GdClassMemberReference) {
            attribute = when (val resolved = reference.resolveDeclaration()) {
                is GdMethodDeclTl -> {
                    if (resolved.containingFile.name == "${GdKeywords.GLOBAL_SCOPE}.gd") GdHighlighterColors.GLOBAL_FUNCTION
                    else if (resolved.isStatic) GdHighlighterColors.STATIC_METHOD_CALL
                    else GdHighlighterColors.METHOD_CALL
                }

                is GdClassVarDeclTl -> {
                    if (resolved.containingFile.name == "${GdKeywords.GLOBAL_SCOPE}.gd") GdHighlighterColors.GLOBAL_VARIABLE_BUILT_IN
                    else GdHighlighterColors.MEMBER
                }

                is PsiFile, is GdClassDeclTl, is GdClassNaming -> {
                    var psi = resolved
                    if (resolved is GdClassNaming) {
                        psi = psi.parent!!
                    }

                    if (ProjectAutoloadUtil.findFromAlias(txt, element) != null) {
                        GdHighlighterColors.GLOBAL_VARIABLE_AUTOLOAD
                    }
                    else if (psi.containingFile.isInSdk()) {
                        val nextLeaf = element.nextLeaf(true)
                        if (!objectContinuation.contains(nextLeaf.elementType) && psi.childrenOfType<GdMethodDeclTl>()
                                .any { it.isConstructor }
                        ) {
                            holder
                                .newAnnotationGd(
                                    HighlightSeverity.ERROR,
                                    GdScriptBundle.message("annotator.builtin.type.cannot.be.assigned.to.a.variable", txt)
                                )
                                .range(element.textRange)
                                .create()
                            return
                        }
                        GdHighlighterColors.ENGINE_TYPE
                    } else GdHighlighterColors.CLASS_TYPE
                }

                null -> run {
                    val polyAttr = resolveFromPolySymbols(element)
                    if (polyAttr != null) {
                        return@run polyAttr
                    } else {
                        // Nothing was found in Poly Symbols and PSI was not resolved
                        if (element.text == "new"
                            || GdClassMemberUtil.calledUpon(element)?.returnType == "Dictionary"
                        ) {
                            return@run GdHighlighterColors.MEMBER
                        }

                        val calledUponExpr = GdClassMemberUtil.calledUpon(element)
                        // For undefined types do not mark it as error
                        if (calledUponExpr != null) {
                            // If qualifier is a node path, skip error
                            if (PsiTreeUtil.findChildOfType(calledUponExpr, GdNodePath::class.java) != null)
                                return@run GdHighlighterColors.MEMBER

                            // If qualifier resolves to a named enum, allow enum member access only for existing enum values
                            run {
                                val decl = GdClassMemberUtil.findDeclaration(calledUponExpr)
                                if (decl is GdEnumDeclTl) {
                                    val name = element.text
                                    val isMember = decl.enumValueList.any { it.enumValueNmi.name == name }
                                    if (isMember) return@run GdHighlighterColors.MEMBER
                                    // otherwise, fall through to unresolved reference error
                                }
                            }

                            val callType = calledUponExpr.returnType
                            if (callType in unresolvedTolerantTypes)
                                return@run GdHighlighterColors.MEMBER
                        }

                        if (element.getCallExpr() != null && GdClassMemberUtil.hasMethodCheck(element))
                            return@run GdHighlighterColors.METHOD_CALL

                        holder
                            .newAnnotationGd(
                                GdProjectState.selectedLevel(state),
                                GdScriptBundle.message("annotator.message.reference.not.found", element.text)
                            )
                            .range(element.textRange)
                            .create()
                        return
                    }
                }

                else -> GdHighlighterColors.MEMBER
            }
        }

        if (attribute == GdHighlighterColors.MEMBER && element.getCallExpr() != null) {
            attribute = GdHighlighterColors.METHOD_CALL
        }

        holder
            .newSilentAnnotation(HighlightSeverity.INFORMATION)
            .range(element.textRange)
            .textAttributes(attribute)
            .create()
    }

    /**
     * Tries to color [element] based on Poly Symbol resolution.
     *
     * Returns:
     *  - the [com.intellij.openapi.editor.colors.TextAttributesKey] to use, or
     *  - `null` if no poly symbol matched.
     */
    private fun resolveFromPolySymbols(
        element: GdRefIdRef,
    ): TextAttributesKey? {
        val symbol = element.resolveSymbolReference() as? GdPolySymbol
        if (symbol == null) return null

        return when (symbol.kind) {
            GdPolySymbolKind.CONSTRUCTOR -> GdHighlighterColors.METHOD_CALL

            GdPolySymbolKind.METHOD -> {
                if (symbol.declaringClassId == GdKeywords.GLOBAL_SCOPE) GdHighlighterColors.GLOBAL_FUNCTION
                else if (symbol.modifiers.contains(GdPolySymbolModifier.STATIC)) GdHighlighterColors.STATIC_METHOD_CALL
                else GdHighlighterColors.METHOD_CALL
            }

            GdPolySymbolKind.PROPERTY -> {
                if (symbol.declaringClassId == GdKeywords.GLOBAL_SCOPE) GdHighlighterColors.GLOBAL_VARIABLE_BUILT_IN
                else GdHighlighterColors.MEMBER
            }
            GdPolySymbolKind.CONSTANT,
            GdPolySymbolKind.SIGNAL,
            GdPolySymbolKind.ENUM,
            GdPolySymbolKind.ENUM_VALUE -> {
                GdHighlighterColors.MEMBER
            }

            GdPolySymbolKind.CLASS -> {
                if (symbol is GdSdkPolySymbol) {
                    GdHighlighterColors.ENGINE_TYPE
                } else GdHighlighterColors.CLASS_TYPE
            }

            GdPolySymbolKind.AUTOLOAD -> GdHighlighterColors.GLOBAL_VARIABLE_AUTOLOAD
            GdPolySymbolKind.LOADED_CLASS_ALIAS -> GdHighlighterColors.CLASS_TYPE


            else -> GdHighlighterColors.MEMBER
        }
    }
}
