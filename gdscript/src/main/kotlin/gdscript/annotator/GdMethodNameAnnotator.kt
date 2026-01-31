package gdscript.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import gdscript.GdScriptBundle
import gdscript.highlighter.GdHighlighterColors
import gdscript.psi.GdClassVarDeclTl
import gdscript.psi.GdConstDeclTl
import gdscript.psi.GdEnumDeclTl
import gdscript.psi.GdMethodDeclTl
import gdscript.psi.GdMethodIdNmi
import gdscript.psi.GdSignalDeclTl
import gdscript.psi.utils.GdClassMemberUtil
import gdscript.psi.utils.GdClassUtil

/**
 * Colors method name
 * Checks for uniqueness
 */
class GdMethodNameAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element !is GdMethodIdNmi) return

        holder
            .newSilentAnnotation(HighlightSeverity.INFORMATION)
            .range(element.textRange)
            .textAttributes(GdHighlighterColors.METHOD_DECLARATION)
            .create()

        isUnique(element, holder)
    }

    private fun isUnique(element: GdMethodIdNmi, holder: AnnotationHolder) {
        val name = element.name

        // Constructors
        if (GdClassUtil.getOwningClassName(element) == name) {
            return
        }

        val declaration = GdClassMemberUtil.listLocalDeclarationsUpward(element)[name]
        if (declaration != null) {
            val type = when (declaration) {
                is GdMethodDeclTl -> "method"
                is GdClassVarDeclTl -> "variable"
                is GdConstDeclTl -> "constant"
                is GdSignalDeclTl -> "signal"
                is GdEnumDeclTl -> "enum"
                else -> "member"
            }

            holder
                .newAnnotationGd(
                    element.project,
                    HighlightSeverity.ERROR,
                    GdScriptBundle.message("annotator.name.already.defined.as.type", element.name, type)
                )
                .range(element.textRange)
                .create()
        }
    }

}
