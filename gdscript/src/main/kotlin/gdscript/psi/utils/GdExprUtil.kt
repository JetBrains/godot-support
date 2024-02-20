package gdscript.psi.utils

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.jetbrains.rd.util.firstOrNull
import gdscript.GdKeywords
import gdscript.psi.utils.GdClassMemberUtil.constructors
import gdscript.utils.StringUtil.parseFromSquare

object GdExprUtil {

    fun typeAccepts(from: String, into: String, element: PsiElement): Boolean {
        return typeAccepts(from, into, element.project)
    }

    fun typeAccepts(from: String, into: String, project: Project): Boolean {
        if (from == into) return true
        if (from.isBlank() || into.isBlank()) return true
        if (into == "void") return false

        // left = right
        var left = into
        var right = from

        var arrays = 0
        if (from.startsWith("Array")) arrays++
        if (into.startsWith("Array")) arrays++

        // Only 1 is an array
        if (arrays == 1) {
            return allowedExceptions(left, right, project)
        } else if (arrays > 1) {
            left = left.parseFromSquare()
            right = right.parseFromSquare()
        }

        if (allowedExceptions(left, right, project)) return true

        val classId = GdClassUtil.getClassIdElement(left, project) ?: return true
        val classElement = GdClassUtil.getOwningClassElement(classId)

        // Constructor
        GdClassMemberUtil
            .listClassMemberDeclarations(classElement, constructors = true)
            .constructors()
            .forEach {
                if (it.parameters.firstOrNull()?.value == right) return true
            }

        // Inheritance
        val currentClassId = GdClassUtil.getClassIdElement(right, project) ?: return true
        val currentClassElement = GdClassUtil.getOwningClassElement(currentClassId)

        if (GdInheritanceUtil.isExtending(currentClassElement, left)) return true

        return false
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
