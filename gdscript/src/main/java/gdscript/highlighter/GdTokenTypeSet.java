package gdscript.highlighter;

import com.intellij.psi.tree.TokenSet;
import gdscript.psi.GdTypes;

public interface GdTokenTypeSet
{

    // Red
    TokenSet KEYWORDS = TokenSet.create(
        GdTypes.CLASS_NAME,
//        GdTypes.CLASS,
        GdTypes.EXTENDS,
        GdTypes.TOOL,
        GdTypes.FUNC,
        GdTypes.ENUM,
        GdTypes.SELF,
        GdTypes.CONST,
        GdTypes.VAR,
        GdTypes.INT,
        GdTypes.FLOAT,
        GdTypes.BOOL,
        GdTypes.STR,
        GdTypes.TRUE,
        GdTypes.FALSE,
        GdTypes.VOID,
        GdTypes.WHILE,
        GdTypes.BREAKPOINT,
//        GdTypes.STATIC,
        GdTypes.SIGNAL,
        GdTypes.SETGET,
        GdTypes.FOR,
        GdTypes.IN,
        GdTypes.MATCH,
        GdTypes.ASSERT,
        GdTypes.AWAIT,
//        GdTypes.PRELOAD,
        GdTypes.AS,
        GdTypes.IS,
        GdTypes.AND,
        GdTypes.OR,
        GdTypes.NEGATE,
        GdTypes.NOT,
        GdTypes.PI,
        GdTypes.TAU,
        GdTypes.NAN,
        GdTypes.INF,
        GdTypes.NULL
    );

    // Pink
    TokenSet FLOW_KEYWORDS = TokenSet.create(
        GdTypes.IF,
        GdTypes.ELSE,
        GdTypes.ELIF,
        GdTypes.CONTINUE,
        GdTypes.BREAK,
        GdTypes.RETURN,
        GdTypes.PASS
    );

    // Yellow
    TokenSet STRINGS = TokenSet.create(GdTypes.STRING);

    // Teal
    TokenSet CLASS_TYPE = TokenSet.create(
    );

    // Teal
    TokenSet NUMBERS = TokenSet.create(
            GdTypes.NUMBER
    );

    // White
    TokenSet IDENTIFIERS = TokenSet.create(
            GdTypes.IDENTIFIER
    );

    // Green
    TokenSet ANNOTATIONS = TokenSet.create(
            GdTypes.ANNOTATOR
    );

    // Green
    TokenSet NODE_PATH = TokenSet.create(
            GdTypes.NODE_PATH_LEX
    );

    // Red
    TokenSet BAD_CHARACTERS = TokenSet.create(GdTypes.BAD_CHARACTER);

    // Grey
    TokenSet COMMENT = TokenSet.create(
            GdTypes.COMMENT
    );
}
