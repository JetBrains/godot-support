package tscn.highlighter

import com.intellij.psi.tree.TokenSet
import tscn.psi.TscnTypes

object TscnTokenTypeSet {

    val SYNTAX_TOKENS = TokenSet.create(
        TscnTypes.LSBR,
        TscnTypes.RSBR,
        TscnTypes.LCBR,
        TscnTypes.RCBR,
        TscnTypes.LRBR,
        TscnTypes.RRBR,
        TscnTypes.EQ,
        TscnTypes.COLON,
        TscnTypes.COMMA,
    )

    val STRING_TOKENS = TokenSet.create(
        TscnTypes.STRING
    )

    val RES_STRING_TOKENS = TokenSet.create(
        TscnTypes.STRING_REF
    )

    val ATTRIBUTE_TOKENS = TokenSet.create(
        TscnTypes.DATA_LINE_NM,
        TscnTypes.HEADER_VALUE_NM,
    )

    val RESOURCE_TOKENS = TokenSet.create(
        TscnTypes.GD_SCENE,
        TscnTypes.EXT_RESOURCE,
        TscnTypes.NODE,
        TscnTypes.CONNECTION,
    )

    val GODOT_MEMBER_REF_TOKENS = TokenSet.create(
        TscnTypes.IDENTIFIER_REF,
    )

    val GODOT_CLASS_REF_TOKENS = TokenSet.create(
        TscnTypes.OBJECT_REF,
    )

    val NUMBER_TOKENS = TokenSet.create(
        TscnTypes.NUMBER,
    )
}
