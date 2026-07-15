package gdscript.psi

import com.intellij.psi.PsiElement

interface GdInheritanceId : PsiElement {
    val inheritanceIdNm: GdInheritanceIdRef

    val inheritanceSubIdNmList: List<GdInheritanceSubIdRef>
}
