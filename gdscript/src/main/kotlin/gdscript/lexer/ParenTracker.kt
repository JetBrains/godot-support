package gdscript.lexer

/* -- ParenTracker: encapsulates bracket depth logic -- */
class ParenTracker {
    var depth: Int = 0

    /* Track bracket types optionally if needed later */
    fun open() {
        depth++
    }

    fun close() {
        if (depth > 0) depth--
    }

    val isTopLevel: Boolean
        get() = depth == 0
    val isNested: Boolean
        get() = depth > 0
}