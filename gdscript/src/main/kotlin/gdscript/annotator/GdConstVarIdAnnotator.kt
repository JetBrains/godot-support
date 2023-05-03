package gdscript.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.*
import gdscript.psi.utils.GdClassMemberUtil

/**
 * Checks for uniqueness of variables
 */
class GdConstVarIdAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element is GdVarNmi) {
            isUnique(element as GdNamedIdElement, holder);
        }
    }

    private fun isUnique(element: GdNamedIdElement, holder: AnnotationHolder) {
        val inMethod = PsiTreeUtil.findFirstParent(element) {
            it is GdClassDeclTl || it is GdMethodDeclTl || it is GdFile
        } is GdMethodDeclTl

        val declaration = GdClassMemberUtil.findDeclaration(element, true, inMethod, true)
        if (declaration != null && declaration !is GdClassNaming) {
            holder
                .newAnnotation(HighlightSeverity.ERROR,
                    "[${element.name}] is already defined")
                .range(element.textRange)
                .create();
        }
    }

}
