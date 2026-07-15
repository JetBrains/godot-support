package gdscript.psi

import com.intellij.psi.PsiElement

interface GdSetDecl : PsiElement {
    val endStmt: GdEndStmt?

    val setMethodIdNm: GdSetMethodIdRef?

    val stmtOrSuite: GdStmtOrSuite?

    val typed: GdTyped?

    val varNmi: GdVarNmi?
}
