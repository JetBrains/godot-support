package gdscript.psi

import com.intellij.psi.PsiElement
import gdscript.psi.types.GdDocumented

interface GdEnumValue : PsiElement, GdDocumented {
    val enumValueNmi: GdEnumValueNmi
}
