package gdscript.psi

import com.intellij.psi.PsiElement

interface GdStmtOrSuite : PsiElement {
    val stmt: GdStmt?

    val suiteList: List<GdSuite>
}
