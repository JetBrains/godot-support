package gdscript.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import gdscript.GdKeywords
import gdscript.highlighter.GdHighlighterColors
import gdscript.index.impl.GdClassIdIndex
import gdscript.psi.GdTypeHint
import gdscript.psi.GdTypeHintNm
import gdscript.reference.GdTypeHintNmReference
import gdscript.utils.PsiFileUtil.isInSdk

/**
 * Checks that given return type is valid (built-in or Class)
 */
class GdTypeHintAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element !is GdTypeHint) return

        val project = element.project
        val typeHints = element.typeHintNmList

        if (invalidType(typeHints.last(), project)) {
            holder
                .newAnnotation(HighlightSeverity.ERROR, "Invalid type")
                .range(element.textRange)
                .create()
        }

        // color all type hints
        typeHints.forEach{ colorTypeHints(it, project, holder)}
    }

    private fun invalidType(element: GdTypeHintNm, project: Project) : Boolean {
        // don't spend time on resolving builtin types
        if (GdKeywords.BUILT_TYPES.contains(element.name)) return false
        return GdTypeHintNmReference(element, project).resolve() == null
    }

    private fun colorTypeHints(element: GdTypeHintNm, project: Project, holder: AnnotationHolder) {
        var color = GdHighlighterColors.CLASS_TYPE
        if (GdKeywords.BUILT_TYPES.contains(element.name)) {
            color = GdHighlighterColors.BASE_TYPE
        } else if (GdClassIdIndex.INSTANCE.getGloballyResolved(element.name, project).firstOrNull()?.containingFile?.isInSdk() == true) {
            color = GdHighlighterColors.ENGINE_TYPE
        }

        holder
            .newSilentAnnotation(HighlightSeverity.INFORMATION)
            .range(element.textRange)
            .textAttributes(color)
            .create()
    }

}
