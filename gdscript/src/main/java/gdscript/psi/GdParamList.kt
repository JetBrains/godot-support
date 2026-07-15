package gdscript.psi

import com.intellij.psi.PsiElement

interface GdParamList : PsiElement {
    val paramList: List<GdParam>
}
