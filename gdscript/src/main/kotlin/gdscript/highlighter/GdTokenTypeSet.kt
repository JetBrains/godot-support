package gdscript.highlighter

import com.intellij.psi.tree.TokenSet
import gdscript.psi.GdTypes

interface GdTokenTypeSet {
    companion object {
        // Red
        val KEYWORDS = TokenSet.create(
            GdTypes.CLASS_NAME,
            GdTypes.CLASS,
            GdTypes.EXTENDS,
            GdTypes.FUNC,
            GdTypes.ENUM,
            GdTypes.SELF,
            GdTypes.SUPER,
            GdTypes.CONST,
            GdTypes.VAR,
            GdTypes.TRUE,
            GdTypes.FALSE,
            GdTypes.VOID,
            GdTypes.WHILE,
            GdTypes.BREAKPOINT,
            GdTypes.STATIC,
            GdTypes.VARARG,
            GdTypes.SIGNAL,
            GdTypes.FOR,
            GdTypes.IN,
            GdTypes.MATCH,
            GdTypes.AWAIT,
//            GdTypes.PUPPET,
//            GdTypes.MASTER,
            GdTypes.AS,
            GdTypes.IS,
            GdTypes.AND,
            GdTypes.OR,
            GdTypes.NEGATE,
            GdTypes.NOT,
            GdTypes.NAN,
            GdTypes.INF,
            GdTypes.NULL,
            GdTypes.GET,
            GdTypes.SET
        )

        // Pink
        val FLOW_KEYWORDS = TokenSet.create(
            GdTypes.IF,
            GdTypes.ELSE,
            GdTypes.ELIF,
            GdTypes.CONTINUE,
            GdTypes.BREAK,
            GdTypes.RETURN,
            GdTypes.PASS
        )

        // Yellow
        val STRINGS = TokenSet.create(GdTypes.STRING)

        // Teal
        val CLASS_TYPE = TokenSet.create()

        // Teal
        val NUMBERS = TokenSet.create(
            GdTypes.NUMBER
        )

        // White
        val IDENTIFIERS = TokenSet.create(
            GdTypes.IDENTIFIER
        )

        // Green
        val ANNOTATIONS = TokenSet.create(
            GdTypes.ANNOTATOR
        )

        // Green
        val NODE_PATH = TokenSet.create(
            GdTypes.NODE_PATH_LEX
        )

        // Red
        val BAD_CHARACTERS = TokenSet.create(GdTypes.BAD_CHARACTER)

        // Grey
        val COMMENT = TokenSet.create(
            GdTypes.COMMENT
        )
    }
}
