package gdscript.completion

import com.intellij.patterns.PlatformPatterns.psiElement
import gdscript.psi.GdTypes

object GdCommonContribution {

    val COMMENT_POS = psiElement(GdTypes.COMMENT)

}
