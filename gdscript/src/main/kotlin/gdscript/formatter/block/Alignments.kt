package gdscript.formatter.block

import com.intellij.formatting.Alignment
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import gdscript.formatter.GdCodeStyleSettings
import gdscript.psi.GdTypes

class Alignments {

    companion object {
        val ASSIGN = arrayOf(GdTypes.EQ, GdTypes.ASSIGN, GdTypes.ASSIGN_TYPED)
    }

    var standard: Alignment? = null;
    var after: Alignment? = null;
    var settings: GdCodeStyleSettings;
    var spaces = 0;

    constructor(settings: GdCodeStyleSettings, standard: Alignment? = null, after: Alignment? = null) {
        this.standard = standard
        this.after = after
        this.settings = settings
    }

    fun initialize() {
        standard = Alignment.createAlignment(true);
        after = Alignment.createAlignment(true);
        spaces = 0;
    }

    /**
     * Check whether to reset alignments (required at least two white_spaces or any non-aligned element)
     */
    fun reset(type: IElementType) {
        if (type == TokenType.WHITE_SPACE) {
            if (++spaces >= 2) {
                initialize();
            }
        } else if (!GdAlignments.ALIGN_SCOPE.contains(type)) {
            initialize();
        } else {
            spaces = 0;
        }
    }

    fun clone(type: IElementType): Alignments {
        if (GdAlignments.ALIGN_SCOPE.contains(type)) {
            return Alignments(settings, standard, after)
        }

        return Alignments(settings);
    }

    fun getAlignment(type: IElementType): Alignment? {
        var returnAlign: Alignment? = null;
        if (standard != null) {
            if (ASSIGN.contains(type)) {
                if (settings.ALIGN_ASSIGNMENTS) {
                    returnAlign = standard;
                }
                standard = null;
            }

        } else {
            if (settings.ALIGN_AFTER_ASSIGNMENTS) {
                returnAlign = after;
            }
            after = null;
        }

        return returnAlign;
    }

}
