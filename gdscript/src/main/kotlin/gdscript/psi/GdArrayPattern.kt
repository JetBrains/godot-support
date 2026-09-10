package gdscript.psi

import com.intellij.psi.PsiElement

interface GdArrayPattern : PsiElement {
    val patternList: List<GdPattern>
}
