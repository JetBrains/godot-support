package gdscript.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import gdscript.GdScriptBundle
import gdscript.psi.GdClassDeclTl
import gdscript.psi.GdClassNaming
import gdscript.psi.GdFile
import gdscript.psi.GdMethodDeclTl
import gdscript.psi.GdNamedIdElement
import gdscript.psi.GdParam
import gdscript.psi.GdPattern
import gdscript.psi.GdSetDecl
import gdscript.psi.GdVarNmi
import gdscript.psi.utils.GdClassMemberUtil

/**
 * Checks for uniqueness of variables
 */
class GdConstVarIdAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element is GdVarNmi) {
            when (element.parent) {
                is GdParam -> return
                is GdSetDecl -> return
                is GdPattern -> return
            }

            isUnique(element as GdNamedIdElement, holder)
        }
    }

    private fun isUnique(element: GdNamedIdElement, holder: AnnotationHolder) {
        val inMethod = PsiTreeUtil.findFirstParent(element) {
            it is GdClassDeclTl || it is GdMethodDeclTl || it is GdFile
        } is GdMethodDeclTl

        val declaration = GdClassMemberUtil.findDeclaration(element, true, inMethod, true)
        if (declaration != null && declaration !is GdClassNaming) {
            holder
                .newAnnotationGd(element.project,
                        HighlightSeverity.ERROR,
                    GdScriptBundle.message("annotator.property.is.already.defined", element.name.toString())
                )
                .range(element.textRange)
                .create()
        }
    }

}
