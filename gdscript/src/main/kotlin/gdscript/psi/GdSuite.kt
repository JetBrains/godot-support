package gdscript.psi

import com.intellij.psi.PsiElement

interface GdSuite : PsiElement {
    val stmtList: List<GdStmt>
}
