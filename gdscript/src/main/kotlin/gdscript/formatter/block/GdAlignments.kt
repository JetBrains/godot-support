package gdscript.formatter.block

import com.intellij.formatting.Alignment
import com.intellij.psi.tree.IElementType
import gdscript.formatter.GdCodeStyleSettings
import gdscript.psi.GdTypes

object GdAlignments {

    val EQ = Alignment.createAlignment(true);
    val COMMENT = Alignment.createAlignment(true);

    fun getAlignment(type: IElementType, settings: GdCodeStyleSettings): Alignment? {
        return when (type) {
            GdTypes.COMMENT -> if (settings.ALIGN_COMMENTS) COMMENT else null
            else -> null
        }

    }

}
