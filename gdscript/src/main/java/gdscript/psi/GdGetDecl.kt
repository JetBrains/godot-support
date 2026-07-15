package gdscript.psi

import com.intellij.psi.PsiElement

interface GdGetDecl : PsiElement {
    val endStmt: GdEndStmt?

    val getMethodIdNm: GdGetMethodIdRef?

    val stmtOrSuite: GdStmtOrSuite?
}
