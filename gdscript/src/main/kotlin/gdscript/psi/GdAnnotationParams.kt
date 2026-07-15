package gdscript.psi

import com.intellij.psi.PsiElement

interface GdAnnotationParams : PsiElement {
    val exprList: List<GdExpr>
}
