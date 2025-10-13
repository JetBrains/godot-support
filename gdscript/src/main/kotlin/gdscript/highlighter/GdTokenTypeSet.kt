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
            GdTypes.BREAKPOINT,
            GdTypes.STATIC,
            GdTypes.VARARG,
            GdTypes.SIGNAL,
            GdTypes.IN,
            GdTypes.MATCH,
            GdTypes.WHEN,
            GdTypes.AWAIT,
            GdTypes.PUPPET,
            GdTypes.MASTER,
            GdTypes.REMOTE,
            GdTypes.REMOTESYNC,
            GdTypes.MASTERSYNC,
            GdTypes.PUPPETSYNC,
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
            GdTypes.SET,
            GdTypes.ANDAND,
            GdTypes.OROR,
        )

        val VOID = TokenSet.create(GdTypes.VOID)

        // Pink
        val FLOW_KEYWORDS = TokenSet.create(
            GdTypes.FOR,
            GdTypes.IF,
            GdTypes.ELSE,
            GdTypes.ELIF,
            GdTypes.CONTINUE,
            GdTypes.WHILE,
            GdTypes.BREAK,
            GdTypes.RETURN,
            GdTypes.PASS,
            GdTypes.WHEN
        )

        // Yellow
        val STRINGS = TokenSet.create(
            GdTypes.STRING,
            GdTypes.NODE_PATH,
        )

        val STRING_FORMAT = TokenSet.create()

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

        val STRING_NAME = TokenSet.create(
            GdTypes.STRING_NAME
        )

        // Green
        val NODE_STRING_PATH = TokenSet.create(
            GdTypes.NODE_PATH_LIT,
        )

        // Blue
        val MEMBER = TokenSet.create()

        // Red
        val BAD_CHARACTERS = TokenSet.create(GdTypes.BAD_CHARACTER)

        // Grey
        val COMMENT = TokenSet.create(
            GdTypes.COMMENT
        )
    }
}
