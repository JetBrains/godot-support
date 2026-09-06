package gdscript.psi

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

interface GdKeyValue : PsiElement {
    val exprList: List<GdExpr>

    val keyNmi: GdKeyNmi?
        get() = PsiTreeUtil.getChildOfType(this, GdKeyNmi::class.java)

    val key: PsiElement?
        /**
         * @return GdKeyNmi or GdExpr
         */
        get() {
            // Identifier and string-literal keys are both wrapped in a GdKeyNmi by the parser,
            // any other key (an arbitrary expression) is the first expr
            val keyNmi = PsiTreeUtil.getChildOfType(this, GdKeyNmi::class.java)
            if (keyNmi != null) return keyNmi

            return this.exprList.firstOrNull()
        }

    val value: PsiElement?
        get() = this.exprList.lastOrNull()
}
