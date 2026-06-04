package gdscript.formatter

import com.intellij.lang.BracePair
import com.intellij.lang.PairedBraceMatcher
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType
import gdscript.formatter.block.GdBlocks
import gdscript.psi.GdTypes

class GdBraceMatcher : PairedBraceMatcher {

    override fun getPairs(): Array<BracePair> = arrayOf(
        BracePair(GdTypes.LRBR, GdTypes.RRBR, true),
        BracePair(GdTypes.LCBR, GdTypes.RCBR, true),
        BracePair(GdTypes.LSBR, GdTypes.RSBR, true),
    )

    override fun isPairedBracesAllowedBeforeType(lbraceType: IElementType, contextType: IElementType?): Boolean {
        return contextType in GdBlocks.WHITE_SPACE
            || contextType === GdTypes.COMMENT
            || contextType === GdTypes.COLON
            || contextType === GdTypes.COMMA
            || contextType === GdTypes.RRBR
            || contextType === GdTypes.RSBR
            || contextType === GdTypes.RCBR
            || contextType === GdTypes.LCBR
            || contextType == null
    }

    override fun getCodeConstructStart(file: PsiFile?, openingBraceOffset: Int): Int {
        return openingBraceOffset
    }

}
