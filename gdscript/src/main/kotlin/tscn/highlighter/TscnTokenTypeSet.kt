package tscn.highlighter

import com.intellij.psi.tree.TokenSet
import tscn.psi.TscnTypes

object TscnTokenTypeSet {

    // Orange
    val SYNTAX_TOKENS = TokenSet.create(
            TscnTypes.LSBR,
            TscnTypes.RSBR,
            TscnTypes.EQ
    )
}
