package tscn.highlighter

import com.intellij.psi.tree.TokenSet
import tscn.psi.TscnTypes

object TscnTokenTypeSet {

    // Orange
    val SYNTAX_TOKENS = TokenSet.create(
            TscnTypes.LSBR,
            TscnTypes.RSBR,
            TscnTypes.LCBR,
            TscnTypes.RCBR,
            TscnTypes.LP,
            TscnTypes.RP,
            TscnTypes.EQ,
            TscnTypes.COLON,
            TscnTypes.COMMA,
            TscnTypes.AMPERSAND
    )

    val STRING_TOKENS = TokenSet.create(
            TscnTypes.STRING_VALUE
    )

    val RES_STRING_TOKENS = TokenSet.create(
            TscnTypes.RES_STRING_VALUE
    )

    val VALUE_TOKENS = TokenSet.create(
            TscnTypes.VALUE
    )

    val ATTRIBUTE_TOKENS = TokenSet.create(
            TscnTypes.HEADER_VALUE_NM, TscnTypes.DATA_LINE_NM
    )

    val RESOURCE_TOKENS = TokenSet.create(
            TscnTypes.GD_SCENE, TscnTypes.EXT_RESOURCE, TscnTypes.NODE, TscnTypes.CONNECTION, TscnTypes.UNKNOWN
    )

    val GODOT_MEMBER_REF_TOKENS = TokenSet.create(
            TscnTypes.GODOT_MEMBER_REF
    )

    val GODOT_CLASS_REF_TOKENS = TokenSet.create(
            TscnTypes.GODOT_CLASS_REF
    )
}
