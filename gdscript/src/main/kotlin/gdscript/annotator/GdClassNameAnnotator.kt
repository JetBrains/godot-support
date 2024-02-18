package gdscript.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import gdscript.action.quickFix.GdFileClassNameAction
import gdscript.action.quickFix.GdRemoveElementsAction
import gdscript.highlighter.GdHighlighterColors
import gdscript.index.impl.GdClassNamingIndex
import gdscript.index.impl.GdFileResIndex
import gdscript.psi.*
import gdscript.psi.utils.GdClassUtil
import gdscript.psi.utils.PsiGdFileUtil
import gdscript.utils.PsiFileUtil.isInSdk
import gdscript.utils.StringUtil.snakeToPascalCase

/**
 * Checks for uniqueness of classes & existing inheritance
 * Colors class names
 */
class GdClassNameAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        when (element) {
            is GdInheritanceId -> existingInheritance(element, holder)
            is GdInheritanceIdNm -> colorInheritance(element, holder)
            is GdInheritanceSubIdNm -> colorClass(element, GdHighlighterColors.CLASS_TYPE, holder)
            is GdClassNameNmi -> {
                alreadyExists(element, holder)
                classNameToFilename(element, holder)
                resourceHasNoInnerClass(element, holder)
            }
            is GdClassNaming -> isDuplicated(element, holder, "class_name")
            is GdInheritance -> isDuplicated(element, holder, "Inheritance")
        }
    }

    private fun existingInheritance(element: GdInheritanceId, holder: AnnotationHolder) {
        if (GdClassUtil.getClassIdElement(element.text, element) == null
            // File index when you are extending script without class_name
            && GdFileResIndex.INSTANCE.getFiles(element.text.trim('"'), element.project).isEmpty()
        ) {
            // Last case is extending InnerClass within same file which does not require FQN
            // and can directly use any at lower level

            holder
                .newAnnotation(HighlightSeverity.ERROR, "Class not found")
                .range(element.textRange)
                .create()
        }
    }

    private fun colorInheritance(element: GdInheritanceIdNm, holder: AnnotationHolder) {
        if (element.isClassName) {
            if (GdClassUtil.getClassIdElement(element.text, element)?.containingFile?.isInSdk() == true)
            colorClass(element, GdHighlighterColors.ENGINE_TYPE, holder)
            else colorClass(element, GdHighlighterColors.CLASS_TYPE, holder)
        }
    }

    private fun colorClass(element: PsiElement, color: TextAttributesKey, holder: AnnotationHolder) {
        holder
            .newSilentAnnotation(HighlightSeverity.INFORMATION)
            .range(element.textRange)
            .textAttributes(color)
            .create()
    }

    private fun alreadyExists(element: GdClassNameNmi, holder: AnnotationHolder) {
        val name = element.name
        var message = "Class defined in global scope"

        var conflict = GdClassNamingIndex.INSTANCE.getGloballyWithoutSelf(element).isNotEmpty()

        if (element.isInner) {
            // Inner class can conflict with local main class
            conflict = conflict || GdClassNamingIndex.INSTANCE.getInFile(element).isNotEmpty()

            // Or with previously defined at the same level
            var prev = element.parent.prevSibling
            while (prev != null && !conflict) {
                if (prev is GdClassDeclTl && prev.classNameNmi?.name == name) {
                    conflict = true
                    message = "Class already defined"
                }
                prev = prev.prevSibling
            }
        }

        if (conflict) {
            holder
                .newAnnotation(HighlightSeverity.ERROR, message)
                .range(element.textRange)
                .create()
        }
    }

    private fun classNameToFilename(element: GdClassNameNmi, holder: AnnotationHolder) {
        if (element.parent !is GdClassNaming) return;

        val name = element.name
        val filename = PsiGdFileUtil.filename(element.containingFile).snakeToPascalCase()
        if (filename != name) {
            holder
                .newAnnotation(HighlightSeverity.WEAK_WARNING, "Class name does not match filename")
                .range(element.textRange)
                .withFix(GdFileClassNameAction(filename, element))
                .create()
        }
    }

    private fun isDuplicated(element: PsiElement, holder: AnnotationHolder, type: String) {
        if (PsiTreeUtil.getPrevSiblingOfType(element, element::class.java) !== null) {
            holder
                .newAnnotation(HighlightSeverity.ERROR, "$type already defined")
                .range(element.textRange)
                .withFix(GdRemoveElementsAction(element))
                .create()
        }
    }

    private fun resourceHasNoInnerClass(element: GdClassNameNmi, holder: AnnotationHolder) {
        if (element.parent !is GdClassDeclTl) return;
// TODO ii
//        val name = element.name;
//        val filename = PsiGdFileUtil.filename(element.containingFile);
//        if (filename.lowercase() != name.lowercase()) {
//            holder
//                .newAnnotation(HighlightSeverity.WEAK_WARNING, "Resource cannot have InnerClass")
//                .range(element.textRange)
//                .withFix(GdFileClassNameAction(filename, element))
//                .create();
//        }
    }

}
