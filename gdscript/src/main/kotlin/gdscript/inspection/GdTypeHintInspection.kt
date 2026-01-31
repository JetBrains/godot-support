package gdscript.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import gdscript.GdKeywords
import gdscript.GdScriptBundle
import gdscript.inspection.fixes.GdAddVariableTypeHintFix
import gdscript.inspection.util.ProblemsHolderExtension.registerError
import gdscript.inspection.util.ProblemsHolderExtension.registerWeakWarning
import gdscript.psi.GdClassVarDeclTl
import gdscript.psi.GdConstDeclSt
import gdscript.psi.GdConstDeclTl
import gdscript.psi.GdVarDeclSt
import gdscript.psi.GdVisitor
import gdscript.settings.GdProjectSettingsState

class GdTypeHintInspection : LocalInspectionTool() {

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        val fullType = !GdProjectSettingsState.getInstance(holder.project).state.shortTyped

        return object : GdVisitor() {
            override fun visitClassVarDeclTl(o: GdClassVarDeclTl) {
                checkTypeHint(o, holder, fullType)
            }

            override fun visitConstDeclTl(o: GdConstDeclTl) {
                checkTypeHint(o, holder, fullType)
            }

            override fun visitVarDeclSt(o: GdVarDeclSt) {
                checkTypeHint(o, holder, fullType)
            }

            override fun visitConstDeclSt(o: GdConstDeclSt) {
                checkTypeHint(o, holder, fullType)
            }
        }
    }

    private fun checkTypeHint(element: PsiElement, holder: ProblemsHolder, fullType: Boolean) {
        val returnTypes = when (element) {
            is GdConstDeclTl -> Triple(element.assignTyped, element.typed, element.expr)
            is GdClassVarDeclTl -> Triple(element.assignTyped, element.typed, element.expr)
            is GdVarDeclSt -> Triple(element.assignTyped, element.typed, element.expr)
            is GdConstDeclSt -> Triple(element.assignTyped, element.typed, element.expr)
            else -> return
        }

        val assigment = returnTypes.first
        // := assigment cannot specify the type
        if (assigment !== null && assigment.text.equals(":=")) {
            if (returnTypes.second === null) {
                return
            }

            holder.registerError(
                element, GdScriptBundle.message("inspection.type.hint.assignment.cannot.be.typed"),
                // TODO GdAddVariableTypeHintFix(element, realType, fullType)
            )
            return
        }

        val returnType = returnTypes.second?.typedVal?.returnType
        val expr = returnTypes.third
        if (returnType != null || expr == null) return

        val realType = expr.returnType
        if (realType.isEmpty()) return
        if (realType == GdKeywords.VARIANT || realType == GdKeywords.NULL) return
        if (realType.startsWith("res")) return

        holder.registerWeakWarning(
            element, GdScriptBundle.message("inspection.type.hint.field.return.type.can.be.specified.as", realType),
            GdAddVariableTypeHintFix(element, realType, fullType)
        )
    }

}
