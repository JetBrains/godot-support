package gdscript.psi

import com.intellij.psi.PsiElement
import gdscript.psi.types.GdDocumented

interface GdConstDeclSt : GdStmt, GdDocumented {
    val assignTyped: GdAssignTyped?

    val endStmt: GdEndStmt?

    val expr: GdExpr?

    val typed: GdTyped?

    val varNmi: GdVarNmi?

    fun getName(): String

    val returnType: String

    val returnExpr: PsiElement?
}
