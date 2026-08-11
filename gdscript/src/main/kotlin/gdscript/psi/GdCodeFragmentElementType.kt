package gdscript.psi

import com.intellij.lang.ASTNode
import com.intellij.lang.PsiBuilderFactory
import com.intellij.psi.impl.source.tree.ICodeFragmentElementType
import com.jetbrains.rider.godot.community.gdscript.GdLanguage
import gdscript.GdLexerAdapter
import gdscript.GdScriptBundle
import gdscript.parser.GdPsiBuilder
import gdscript.parser.stmt.GdExStmtParser

/**  Element type of [GdPsiCodeFragment]. */
class GdCodeFragmentElementType(debugName: String) : ICodeFragmentElementType(debugName, GdLanguage) {

    /**
     * Parsed as `EXPR_ST` + `END_STMT`, exactly like in `.gd`; anything past that first expression becomes a single error element.
     */
    override fun parseContents(chameleon: ASTNode): ASTNode? {
        // 1. Builder.
        // Tree parent first: chameleon.psi throws AssertionError on the detached FileElement of a reparse.
        val treeParent = chameleon.treeParent
        val psi = if (treeParent != null) treeParent.psi else chameleon.psi
        checkNotNull(psi) { "Cannot parse a GDScript code fragment without PSI: $chameleon" }
        val builder = PsiBuilderFactory.getInstance().createBuilder(
            psi.project, chameleon, GdLexerAdapter(), chameleon.elementType.language, chameleon.chars,
        )
        val b = GdPsiBuilder(builder)

        // Closed with the chameleon's type at the very end; the outermost marker is discarded by the platform.
        val root = builder.mark()

        // Consume leading whitespace, which is lexed as INDENT
        while (builder.tokenType === GdTypes.INDENT) builder.advanceLexer()

        // 2. One expression statement; GdRecovery.stmt is skipped
        b.enterSection(GdTypes.EXPR_ST)
        // pinned() to keep a partial EXPR_ST, instead of rolling the whole text back
        val parsed = (GdExStmtParser.parse(b, 1, false) || b.pinned()) &&
                     (GdExStmtParser.parseEndStmt(b, consumeNewLine = true) || b.pinned())
        b.exitSection(parsed, true)

        // Consume trailing whitespace, which is lexed as DEDENT or NEW_LINE
        while (builder.tokenType === GdTypes.NEW_LINE || builder.tokenType === GdTypes.DEDENT) builder.advanceLexer()

        // 3. Leftovers: trailing junk becomes one error; eof() skips whitespace/comments, so a trailing comment is fine
        if (!builder.eof()) {
            val leftover = builder.mark()
            while (!builder.eof()) builder.advanceLexer()
            leftover.error(GdScriptBundle.message("parsing.error.fragment.expected.single.expression"))
        }

        root.done(chameleon.elementType)
        return builder.treeBuilt.firstChildNode
    }
}
