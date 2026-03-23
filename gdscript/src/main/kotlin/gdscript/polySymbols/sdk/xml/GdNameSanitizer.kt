package gdscript.sdk.xml

import gdscript.GdKeywords

object GdNameSanitizer {

    private val RESERVED_KEYWORDS = setOf(
        GdKeywords.EXTENDS, GdKeywords.CLASS_NAME, GdKeywords.VAR, GdKeywords.CONST,
        GdKeywords.ENUM, GdKeywords.FUNC, GdKeywords.PASS, GdKeywords.TRUE, GdKeywords.FALSE, GdKeywords.NULL, GdKeywords.SELF,
        GdKeywords.CONTINUE, GdKeywords.BREAKPOINT, GdKeywords.BREAK, GdKeywords.FLOW_RETURN, GdKeywords.VOID,
        GdKeywords.SIGNAL, GdKeywords.IN, GdKeywords.IF, GdKeywords.ELSE, GdKeywords.ELIF,
        GdKeywords.AS, GdKeywords.IS, GdKeywords.WHILE, GdKeywords.FOR, GdKeywords.MATCH, GdKeywords.AWAIT, GdKeywords.STATIC,
        GdKeywords.VARARG, GdKeywords.CLASS, GdKeywords.SUPER, GdKeywords.NOT, GdKeywords.AND, GdKeywords.OR,
    )

    fun sanitizeClassName(name: String): String =
        if (name.startsWith("@")) "_" + name.substring(1) else name

    fun sanitizeParameterName(name: String): String =
        if (name in RESERVED_KEYWORDS) "_$name" else name
}
