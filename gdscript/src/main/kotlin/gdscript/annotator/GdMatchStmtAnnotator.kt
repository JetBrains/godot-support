package gdscript.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.actions.EditorActionUtil
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import gdscript.action.quickFix.GdAddMatchBranchesFix
import gdscript.highlighter.GdHighlighterColors
import gdscript.psi.*
import gdscript.reference.GdClassMemberReference
import gdscript.reference.GdTypeHintNmReference

/**
 * Offers to fill missing branches into Enum match statement
 */
class GdMatchStmtAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element !is GdExpr || element.parent !is GdMatchSt) return;

        val id = PsiTreeUtil.getDeepestLast(element).parent;
        val rootDecl = GdClassMemberReference(id).resolveDeclaration() ?: return;
        val typeHint = PsiTreeUtil.findChildOfType(rootDecl, GdTypeHintNm::class.java) ?: return;
        val enumNmi = GdTypeHintNmReference(typeHint).resolve() ?: return;
        if (enumNmi !is GdEnumDeclNmi) return;

        val match: GdMatchSt = element.parent as GdMatchSt;
        val usedKeys = PsiTreeUtil.findChildrenOfAnyType(match, GdPattern::class.java).map {
            if (it.text == "_") return;
            it.text.split(".").last()
        };

        val enum = PsiTreeUtil.getStubOrPsiParent(enumNmi) as GdEnumDeclTl;
        val allKeys = enum.values.toMutableMap();
        usedKeys.forEach { allKeys.remove(it) };

        if (usedKeys.isEmpty()) return;
        holder
            .newAnnotation(HighlightSeverity.WEAK_WARNING, "Missing enum options")
            .range(element.textRange)
            .withFix(GdAddMatchBranchesFix(match, allKeys.keys.toTypedArray()))
            .create();
    }

}
