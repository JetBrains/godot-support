package gdscript.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.childrenOfType
import com.intellij.psi.util.elementType
import com.intellij.psi.util.nextLeaf
import gdscript.GdKeywords
import gdscript.highlighter.GdHighlighterColors
import gdscript.psi.*
import gdscript.psi.utils.GdClassMemberUtil
import gdscript.reference.GdClassMemberReference
import gdscript.settings.GdProjectSettingsState
import gdscript.settings.GdProjectState
import gdscript.utils.PsiElementUtil.getCallExpr
import gdscript.utils.PsiFileUtil.isInSdk

/**
 * Colors references
 * Checks for existence
 */
class GdRefIdAnnotator : Annotator {

    private val objectContinuation = setOf(GdTypes.LRBR, GdTypes.LSBR, GdTypes.DOT)

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        val state = GdProjectSettingsState.getInstance(element).state.annotators
        if (element !is GdRefIdRef) return
        val txt = element.text

        if (txt == GdKeywords.SELF || txt == GdKeywords.SUPER) return;
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
                    if (resolved.containingFile.name.endsWith("GlobalScope.gd")) GdHighlighterColors.GLOBAL_FUNCTION
                    else if (resolved.isStatic) GdHighlighterColors.STATIC_METHOD_CALL
                    else GdHighlighterColors.METHOD_CALL
                }

                is PsiFile, is GdClassDeclTl, is GdClassNaming -> {
                    var psi = resolved
                    if (resolved is GdClassNaming) {
                        psi = psi.parent!!
                    }

                    if (psi.containingFile.isInSdk()) {
                        var nextLeaf = element.nextLeaf(true)
                        if (!objectContinuation.contains(nextLeaf.elementType) && psi.childrenOfType<GdMethodDeclTl>().any { it.isConstructor }) {
                            holder
                                .newAnnotationGd(element.project, HighlightSeverity.ERROR, "Builtin type $txt cannot be assigned to a variable")
                                .range(element.textRange)
                                .create()
                            return
                        }
                        GdHighlighterColors.ENGINE_TYPE
                    }
                    else GdHighlighterColors.CLASS_TYPE
                }

                null -> run {
                    if (element.text == "new"
                        || GdClassMemberUtil.calledUpon(element)?.returnType == "Dictionary"
                    ) {
                        return@run GdHighlighterColors.MEMBER
                    }

                    val calledUponType = GdClassMemberUtil.calledUpon(element)
                    // For undefined types do not mark it as error
                    if (calledUponType != null) {
                        if (PsiTreeUtil.findChildOfType(calledUponType, GdNodePath::class.java) != null)
                            return@run GdHighlighterColors.MEMBER

                        val callType = calledUponType.returnType
                        if (arrayOf(GdKeywords.VARIANT, "", "Node", "Resource", "null").contains(callType))
                            return@run GdHighlighterColors.MEMBER
                    }

                    if (element.getCallExpr() != null && GdClassMemberUtil.hasMethodCheck(element))
                        return@run GdHighlighterColors.METHOD_CALL

                    holder
                        .newAnnotationGd(element.project, GdProjectState.selectedLevel(state), "Reference [${element.text}] not found")
                        .range(element.textRange)
                        .create()
                    return
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

}
