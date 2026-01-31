package gdscript.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import gdscript.GdScriptBundle
import gdscript.action.quickFix.GdAddMatchBranchesFix
import gdscript.psi.GdEnumDeclNmi
import gdscript.psi.GdEnumDeclTl
import gdscript.psi.GdExpr
import gdscript.psi.GdMatchSt
import gdscript.psi.GdPattern
import gdscript.psi.GdRefIdRef
import gdscript.psi.GdTypeHintRef
import gdscript.psi.utils.GdClassUtil
import gdscript.psi.utils.GdInheritanceUtil
import gdscript.reference.GdClassMemberReference
import gdscript.utils.PsiReferenceUtil.resolveRef

/**
 * Offers to fill missing branches into Enum match statement
 */
class GdMatchStmtAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element !is GdExpr || element.parent !is GdMatchSt) return

        val id = PsiTreeUtil.getDeepestLast(element).parent
        if (id !is GdRefIdRef) return

        val rootDecl = GdClassMemberReference(id).resolveDeclaration() ?: return
        val typeHint = PsiTreeUtil.findChildrenOfType(rootDecl, GdTypeHintRef::class.java).lastOrNull() ?: return
        val enumNmi = typeHint.resolveRef() ?: return
        // val enumNmi = GdTypeHintNmReference(typeHint).resolve() ?: return
        if (enumNmi !is GdEnumDeclNmi) return

        val match: GdMatchSt = element.parent as GdMatchSt
        val usedKeys = PsiTreeUtil.findChildrenOfAnyType(match, GdPattern::class.java).map {
            if (it.text == "_") return
            it.text
        }

        val enum = PsiTreeUtil.getStubOrPsiParent(enumNmi) as GdEnumDeclTl
        val allKeys = enum.values.toMutableMap()

        var prefix = ""
        val owningClassId = GdClassUtil.getOwningClassName(enum)

        // Is it my own enum?
        if (!GdInheritanceUtil.isExtending(element, owningClassId)) {
            val myId = GdClassUtil.getFullClassId(element)
            // InnerClass enum
            if ("$myId.$owningClassId" == GdClassUtil.getFullClassId(enum)) {
                prefix = "$owningClassId."
            } else {
                // Enum of outside class
                val enumClass = GdClassUtil.getOwningClassElement(enum)
                prefix = "${GdClassUtil.getFullClassId(enumClass)}."
            }
        }

        prefix += "${enum.name}."

        usedKeys.forEach {
            if (it.startsWith(prefix)) {
                allKeys.remove(it.removePrefix(prefix))
            }
        }

        if (allKeys.isEmpty()) return
        holder
            .newAnnotationGd(element.project, HighlightSeverity.WEAK_WARNING, GdScriptBundle.message("annotator.missing.enum.options"))
            .range(element.textRange)
            .withFix(GdAddMatchBranchesFix(match, allKeys.keys.map { "$prefix$it" }.toTypedArray()))
            .create()
    }

}
