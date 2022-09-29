package gdscript.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import com.intellij.psi.search.GlobalSearchScope
import gdscript.action.GdFileClassNameAction
import gdscript.highlighter.GdHighlighterColors
import gdscript.index.impl.GdClassNamingIndex
import gdscript.psi.GdClassNameNm
import gdscript.psi.GdInheritanceIdNmi
import gdscript.psi.utils.PsiGdFileUtil

class GdClassNameAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element is GdInheritanceIdNmi) {
            holder
                .newSilentAnnotation(HighlightSeverity.INFORMATION)
                .range(element.textRange)
                .textAttributes(GdHighlighterColors.CLASS_TYPE)
                .create();

            if (GdClassNamingIndex.getGlobally(element).isNotEmpty()) {
                return;
            }

            holder
                .newAnnotation(HighlightSeverity.ERROR, "Unknown class")
                .range(element.textRange)
                .create();
        } else if (element is GdClassNameNm) {
            val name = element.name.orEmpty();
            val filename = PsiGdFileUtil.filename(element.containingFile);
            if (filename.toLowerCase() != name.toLowerCase()) {
                holder
                    .newAnnotation(HighlightSeverity.WEAK_WARNING, "Class name does not match filename")
                    .range(element.textRange)
                    .withFix(GdFileClassNameAction(filename, element))
                    .create();
            }

            if (GdClassNamingIndex.get(
                    name, element.project, GlobalSearchScope.notScope(
                        GlobalSearchScope.fileScope(element.containingFile)
                    )
                ).isEmpty()
            ) {
                return;
            }

            holder
                .newAnnotation(HighlightSeverity.ERROR, "Class already exists")
                .range(element.textRange)
                .withFix(GdFileClassNameAction(filename, element))
                .create();
        }
    }

}
