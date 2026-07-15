package gdscript.psi

import com.intellij.psi.PsiElement

interface GdEmptyStmt : PsiElement {
    val endStmt: GdEndStmt?
}
