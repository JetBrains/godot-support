package com.jetbrains.godot.test.project

/** The source text after breakpoint marker extraction. */
data class ParsedBreakpointMarkers(
    val text: String,
    val lines: Map<String, Int>,
)

/**
 * Extracts GDScript breakpoint markers without changing the line count.
 *
 * The bare `# <bp>` marker has the name `bp`. Other angle-bracket markup stays unchanged.
 */
fun parseBreakpointMarkers(text: String): ParsedBreakpointMarkers {
    val lines = linkedMapOf<String, Int>()
    BREAKPOINT_MARKER.findAll(text).forEach { match ->
        val name = match.groups[1]?.value ?: "bp"
        require(lines.put(name, text.lineNumberAt(match.range.first)) == null) {
            "The breakpoint marker '$name' occurs more than once"
        }
    }
    return ParsedBreakpointMarkers(BREAKPOINT_MARKER.replace(text, ""), lines)
}

private val BREAKPOINT_MARKER = Regex("# <bp(?::([A-Za-z0-9_.-]+))?>")

private fun String.lineNumberAt(offset: Int): Int = 1 + take(offset).count { it == '\n' }
