package gdscript.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import gdscript.action.GdFileClassNameAction
import gdscript.highlighter.GdHighlighterColors
import gdscript.index.impl.GdClassIdIndex
import gdscript.index.impl.GdClassNamingIndex
import gdscript.index.impl.GdFileResIndex
import gdscript.psi.GdClassDeclTl
import gdscript.psi.GdClassNameNmi
import gdscript.psi.GdClassNaming
import gdscript.psi.GdInheritanceId
import gdscript.psi.GdInheritanceIdNm
import gdscript.psi.GdInheritanceSubId
import gdscript.psi.utils.PsiGdFileUtil

/**
 * Checks for uniqueness of classes & existing inheritance
 * Colors class names
 */
class GdClassNameAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        when (element) {
            is GdInheritanceId -> existingInheritance(element, holder);
            is GdInheritanceIdNm -> colorInheritance(element, holder);
            is GdInheritanceSubId -> colorClass(element, holder);
            is GdClassNameNmi -> {
                alreadyExists(element, holder);
                classNameToFilename(element, holder);
            }
        }
    }

    private fun existingInheritance(element: GdInheritanceId, holder: AnnotationHolder) {
        if (GdClassIdIndex.getGloballyResolved(element.text, element).isEmpty()) {
            holder
                .newAnnotation(HighlightSeverity.ERROR, "Unknown class")
                .range(element.textRange)
                .create();
        }
    }

    private fun colorInheritance(element: GdInheritanceIdNm, holder: AnnotationHolder) {
        if (element.isClassName) {
            colorClass(element, holder);
        }
    }

    private fun colorClass(element: PsiElement, holder: AnnotationHolder) {
        holder
            .newSilentAnnotation(HighlightSeverity.INFORMATION)
            .range(element.textRange)
            .textAttributes(GdHighlighterColors.CLASS_TYPE)
            .create();
    }

    private fun alreadyExists(element: GdClassNameNmi, holder: AnnotationHolder) {
        val name = element.name;
        var message = "Class defined in global scope";

        var conflict = GdClassNamingIndex.getGloballyWithoutSelf(element).isNotEmpty();

        if (element.isInner) {
            // Inner class can conflict with local main class
            conflict = conflict || GdClassNamingIndex.getInFile(element).isNotEmpty();

            // Or with previously defined at the same level
            var prev = element.parent.prevSibling;
            while (prev != null && !conflict) {
                if (prev is GdClassDeclTl && prev.classNameNmi?.name == name) {
                    conflict = true;
                    message = "Class already defined";
                }
                prev = prev.prevSibling;
            }
        }

        if (conflict) {
            holder
                .newAnnotation(HighlightSeverity.ERROR, message)
                .range(element.textRange)
                .create();
        }
    }

    private fun classNameToFilename(element: GdClassNameNmi, holder: AnnotationHolder) {
        if (element.parent !is GdClassNaming) return;

        val name = element.name;
        val filename = PsiGdFileUtil.filename(element.containingFile);
        if (filename.lowercase() != name.lowercase()) {
            holder
                .newAnnotation(HighlightSeverity.WEAK_WARNING, "Class name does not match filename")
                .range(element.textRange)
                .withFix(GdFileClassNameAction(filename, element))
                .create();
        }
    }

}
