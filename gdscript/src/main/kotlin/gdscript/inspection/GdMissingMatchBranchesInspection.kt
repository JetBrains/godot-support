package gdscript.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import gdscript.GdScriptBundle
import gdscript.action.quickFix.GdAddMatchBranchesFix
import gdscript.inspection.util.ProblemsHolderExtension.registerWeakWarning
import gdscript.psi.GdEnumDeclNmi
import gdscript.psi.GdEnumDeclTl
import gdscript.psi.GdMatchSt
import gdscript.psi.GdRefIdRef
import gdscript.psi.GdTypeHintRef
import gdscript.psi.GdVisitor
import gdscript.psi.utils.GdClassUtil
import gdscript.psi.utils.GdInheritanceUtil
import gdscript.reference.GdClassMemberReference
import gdscript.utils.PsiReferenceUtil.resolveRef

class GdMissingMatchBranchesInspection : LocalInspectionTool() {

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : GdVisitor() {
            override fun visitMatchSt(match: GdMatchSt) {
                val expr = match.expr ?: return

                val id = PsiTreeUtil.getDeepestLast(expr).parent
                if (id !is GdRefIdRef) return

                if (match.matchBlockList.any { it.stmtOrSuite == null || it.stmtOrSuite?.text?.trim() == "" }) return

                val rootDecl = GdClassMemberReference(id).resolveDeclaration() ?: return
                val typeHint = PsiTreeUtil.findChildrenOfType(rootDecl, GdTypeHintRef::class.java).lastOrNull() ?: return
                val enumNmi = typeHint.resolveRef() ?: return
                if (enumNmi !is GdEnumDeclNmi) return

                val usedKeys = match.matchBlockList.flatMap { block ->
                    block.patternList.patternList.map {
                        if (it.text == "_") return
                        it.text
                    }
                }

                val enum = PsiTreeUtil.getStubOrPsiParent(enumNmi) as GdEnumDeclTl
                val allKeys = enum.values.toMutableMap()

                var prefix = ""
                val owningClassId = GdClassUtil.getOwningClassName(enum)

                if (!GdInheritanceUtil.isExtending(expr, owningClassId)) {
                    val myId = GdClassUtil.getFullClassId(expr)
                    if ("$myId.$owningClassId" == GdClassUtil.getFullClassId(enum)) {
                        prefix = "$owningClassId."
                    } else {
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

                holder.registerWeakWarning(
                    expr,
                    GdScriptBundle.message("inspection.missing.enum.options"),
                    GdAddMatchBranchesFix(match, allKeys.keys.map { "$prefix$it" }.toTypedArray()),
                )
            }
        }
    }
}
