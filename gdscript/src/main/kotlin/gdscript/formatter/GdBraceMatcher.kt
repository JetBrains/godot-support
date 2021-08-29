package gdscript.formatter

import com.intellij.lang.BracePair
import com.intellij.lang.PairedBraceMatcher
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType
import gdscript.psi.GdTypes

class GdBraceMatcher : PairedBraceMatcher {

    override fun getPairs(): Array<BracePair> = arrayOf(
        BracePair(GdTypes.LRBR, GdTypes.RRBR, true),
        BracePair(GdTypes.LCBR, GdTypes.RCBR, true),
        BracePair(GdTypes.LSBR, GdTypes.RSBR, true),
    );

    override fun isPairedBracesAllowedBeforeType(lbraceType: IElementType, contextType: IElementType?): Boolean {
        return true;
    }

    override fun getCodeConstructStart(file: PsiFile?, openingBraceOffset: Int): Int {
        return openingBraceOffset;
    }

}
