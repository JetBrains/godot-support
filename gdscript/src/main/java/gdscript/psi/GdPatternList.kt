package gdscript.psi

import com.intellij.psi.PsiElement

interface GdPatternList : PsiElement {
    val patternList: List<GdPattern>
}
