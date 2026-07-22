package gdscript.psi.utils

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import gdscript.GdKeywords
import gdscript.polySymbols.gdSignature
import gdscript.polySymbols.resolve.GdSymbolResolverUtil
import gdscript.utils.StringUtil.parseFromSquare

object GdExprUtil {

    fun typeAccepts(from: String, into: String, element: PsiElement): Boolean {
        return typeAccepts(from, into, element.project, element)
    }

    fun typeAccepts(from: String, into: String, project: Project, context: PsiElement? = null): Boolean {
        if (from == into) return true
        if (from.isBlank() || into.isBlank()) return true
        if (into == "void") return false

        // left = right
        var left = into
        var right = from

        var arrays = 0
        if (from.startsWith("Array")) arrays++
        if (into.startsWith("Array")) arrays++

        if (arrays > 1) {
            left = left.parseFromSquare()
            right = right.parseFromSquare()
        }

        if (allowedExceptions(left, right, project)) return true

        // Constructor
        // todo: here it is too permissive, just checks that there is a ctor, which accepts "right" type as a first arg - doesn't make sense to me
        val classSymbol = GdSymbolResolverUtil.resolveCanonicalClassSymbol(project, left, context) ?: return true
        GdSymbolResolverUtil.listConstructorSymbols(classSymbol).forEach {
            if (it.gdSignature?.parameters?.firstOrNull()?.type == right) return true
        }

        // Inheritance
        return GdSymbolResolverUtil.isExtendingCanonical(right, project, context, left)
    }

    private fun allowedExceptions(left: String, right: String, project: Project): Boolean {
        if (arrayOf(GdKeywords.VARIANT, "RID").contains(left) ||
                arrayOf(GdKeywords.VARIANT).contains(right)) return true

        if (arrayOf("Node", "Resource").contains(right)) {
            val currentClassId = GdClassUtil.getClassIdElement(left, project)
            if (currentClassId != null) {
                val currentClassElement = GdClassUtil.getOwningClassElement(currentClassId)
                return GdInheritanceUtil.isExtending(currentClassElement, right)
            }
        }

        return false
    }

}
