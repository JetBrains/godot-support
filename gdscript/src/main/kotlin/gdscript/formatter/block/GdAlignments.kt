package gdscript.formatter.block

import com.intellij.formatting.Alignment
import com.intellij.psi.tree.IElementType
import gdscript.formatter.GdCodeStyleSettings
import gdscript.psi.GdTypes

object GdAlignments {

    var alignment: Alignment? = null;
    var type: IElementType? = null;

    fun selectAlignment(type: IElementType, settings: GdCodeStyleSettings, alignments: Alignments): Alignment? {
        return when (type) {
            GdTypes.COMMENT -> if (settings.ALIGN_COMMENTS) alignments.standard else null
            GdTypes.EQ, GdTypes.ASSIGN -> if (settings.ALIGN_ASSIGNMENTS) alignments.standard else null
            else -> null
        }
    }

    fun getAlignment(type: IElementType, settings: GdCodeStyleSettings): Alignment? {
        val currentType = when (type) {
            GdTypes.COMMENT -> if (settings.ALIGN_COMMENTS) GdTypes.COMMENT else null
            GdTypes.EQ, GdTypes.ASSIGN -> if (settings.ALIGN_ASSIGNMENTS) GdTypes.EQ else null
            else -> null
        }
        if (currentType == null) {
            alignment = null
        } else if (currentType != type) {
            alignment = Alignment.createAlignment(true)
        }

        return alignment
    }

    fun reset() {
        alignment = null;
        type = null;
    }

}
