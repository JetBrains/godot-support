package gdscript.psi

import com.intellij.psi.PsiElement

interface GdMatchBlock : PsiElement {
    val patternList: GdPatternList

    val stmtOrSuite: GdStmtOrSuite?
}
