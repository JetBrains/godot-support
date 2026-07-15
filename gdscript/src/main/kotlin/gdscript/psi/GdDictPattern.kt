package gdscript.psi

import com.intellij.psi.PsiElement

interface GdDictPattern : PsiElement {
    val keyValuePatternList: List<GdKeyValuePattern>
}
