package gdscript.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import gdscript.GdKeywords
import gdscript.psi.GdCallEx
import gdscript.psi.GdMethodDeclTl
import gdscript.psi.utils.GdInheritanceUtil
import gdscript.psi.utils.GdMethodUtil

/**
 * Checks that constructor calls _init if parent requires arguments
 */
class GdConstructorAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element !is GdMethodDeclTl) return;
        if (element.name != GdKeywords.INIT_METHOD) return;

        // If parent has parameters - child must call super(args)
        val parent = GdInheritanceUtil.getExtendedElement(element) ?: return;
        val parentConstructor = GdMethodUtil.findMethod(parent, GdKeywords.INIT_METHOD) ?: return;
        if (parentConstructor.parameters.isEmpty()) return;

        val stmts = PsiTreeUtil.findChildrenOfType(element, GdCallEx::class.java)
            .filter{ it.expr.text == GdKeywords.SUPER };

        if (stmts.isEmpty()) {
            holder
                .newAnnotation(HighlightSeverity.ERROR, "Initializing super() constructor is required")
                .range(element.methodIdNmi?.textRange ?: element.textRange)
                .create();
        }
    }

}
