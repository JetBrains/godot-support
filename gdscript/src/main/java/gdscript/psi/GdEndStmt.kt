package gdscript.psi

import com.intellij.psi.PsiElement

interface GdEndStmt : PsiElement {
    val newLineEnd: GdNewLineEnd?
}
