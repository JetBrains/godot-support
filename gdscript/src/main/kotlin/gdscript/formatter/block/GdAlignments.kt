package gdscript.formatter.block

import com.intellij.formatting.Alignment
import com.intellij.psi.tree.IElementType
import gdscript.formatter.GdCodeStyleSettings
import gdscript.psi.GdTypes

// TODO clean up
object GdAlignments {

    val ALIGN_SCOPE = arrayOf(
        GdTypes.CONST_DECL_TL,
        // TODO enum
        // TODO signal
        GdTypes.CLASS_VAR_DECL_TL,

        GdTypes.ASSIGN_ST,
        GdTypes.VAR_DECL_ST,
        GdTypes.CONST_DECL_ST,
        // TODO match?
    )

    var alignment: Alignment? = null;
    var type: IElementType? = null;

    fun reset() {
        alignment = null;
        type = null;
    }

}
