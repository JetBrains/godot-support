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
//        GdTypes.ENUM,
//        GdTypes.SELF,
//        GdTypes.CONST,
//        GdTypes.VAR,
//        GdTypes.SELF,
//        GdTypes.INT,
//        GdTypes.TRUE,
//        GdTypes.FALSE,
//        GdTypes.WHILE,
//        GdTypes.BREAKPOINT,
//        GdTypes.STATIC,
//        GdTypes.SIGNAL,
//        GdTypes.SETGET,
//        GdTypes.FOR,
//        GdTypes.IN,
//        GdTypes.MATCH,
//        GdTypes.ASSERT,
//        GdTypes.YIELD,
//        GdTypes.PRELOAD,
//        GdTypes.AS,
//        GdTypes.AND,
//        GdTypes.OR,
//        GdTypes.NOT,
//        GdTypes.PI,
//        GdTypes.TAU,
//        GdTypes.NAN,
//        GdTypes.INF,
//        GdTypes.NULL
          GdTypes.FUNC
    );

    // Pink
    TokenSet FLOW_KEYWORDS = TokenSet.create(
//        GdTypes.IF,
//        GdTypes.ELSE,
//        GdTypes.ELIF,
//        GdTypes.CONTINUE,
//        GdTypes.BREAK,
//        GdTypes.RETURN,
        GdTypes.PASS
    );

    // Yellow
    TokenSet STRINGS = TokenSet.create(GdTypes.STRING);

    // Teal
    TokenSet NUMBERS = TokenSet.create(
//            GdTypes.NUMBER
    );

    // White
    TokenSet IDENTIFIERS = TokenSet.create(
            GdTypes.IDENTIFIER
    );

    // TODO correct color
    // Green
    TokenSet ANNOTATIONS = TokenSet.create(
//            GdTypes.EXPORT,
//            GdTypes.ONREADY
    );

    // Red
    TokenSet BAD_CHARACTERS = TokenSet.create(GdTypes.BAD_CHARACTER);

    // Grey
    TokenSet COMMENT = TokenSet.create(
            GdTypes.COMMENT
    );
}
