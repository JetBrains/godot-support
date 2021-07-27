package gdscript.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import com.intellij.psi.search.GlobalSearchScope
import gdscript.competion.staticLoader.StaticClassLoader
import gdscript.highlighter.GdHighlighterColors
import gdscript.index.impl.GdClassNamingIndex
import gdscript.psi.GdClassNameNm
import gdscript.psi.GdInheritanceIdNmi

class GdClassNameAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element is GdInheritanceIdNmi) {
            holder
                .newSilentAnnotation(HighlightSeverity.INFORMATION)
                .range(element.textRange)
                .textAttributes(GdHighlighterColors.CLASS_TYPE)
                .create();

            if (GdClassNamingIndex.get(element.name, element.project, GlobalSearchScope.projectScope(element.project))
                    .isNotEmpty()
            ) {
                return;
            }
            if (StaticClassLoader.getClasses()[element.name] !== null) {
                return;
            }

            holder
                .newAnnotation(HighlightSeverity.ERROR, "Unknown class")
                .range(element.textRange)
                .create();
        } else if (element is GdClassNameNm) {
            if (GdClassNamingIndex.get(
                    element.name.orEmpty(), element.project, GlobalSearchScope.notScope(
                        GlobalSearchScope.fileScope(element.containingFile)
                    )
                ).isEmpty()
                && StaticClassLoader.getClasses()[element.name] === null
            ) {
                return;
            }

            holder
                .newAnnotation(HighlightSeverity.ERROR, "Class already exists")
                .range(element.textRange)
                .create();
        }
    }

}
