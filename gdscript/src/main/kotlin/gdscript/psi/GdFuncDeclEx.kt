package gdscript.psi

import com.intellij.psi.PsiElement
import gdscript.psi.types.GdDocumented

interface GdFuncDeclEx : GdExpr, GdDocumented {
    val funcDeclIdNmi: GdFuncDeclIdNmi?

    val paramList: GdParamList?

    val returnHint: GdReturnHint?

    val stmtOrSuite: GdStmtOrSuite?

    val invokedReturnType: String

    val returnExpr: PsiElement?

    val parameters: LinkedHashMap<String, String>
}
