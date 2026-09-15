package com.jetbrains.godot.gdscript.integration

import com.jetbrains.godot.test.project.parseBreakpointMarkers
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class BreakpointMarkersTest {
    @Test
    fun `parser extracts named and bare breakpoint markers`() {
        val source = """
            func run():
                <caret>print("<error>bad</error>") # <bp:first>
                pass # <bp:second>
                return # <bp>
        """.trimIndent()

        val parsed = parseBreakpointMarkers(source)

        assertEquals(mapOf("first" to 2, "second" to 3, "bp" to 4), parsed.lines)
        assertFalse(parsed.text.contains("# <bp>"))
    }

    @Test
    fun `parser preserves platform markup`() {
        val source = "<caret>print(\"<error>bad</error>\") # <bp>"

        val parsed = parseBreakpointMarkers(source)

        assertTrue(parsed.text.contains("<caret>"))
        assertTrue(parsed.text.contains("<error>bad</error>"))
    }

    @Test
    fun `parser preserves the line count`() {
        val source = "first # <bp:first>\nsecond # <bp:second>\nthird"

        val parsed = parseBreakpointMarkers(source)

        assertEquals(source.count { it == '\n' }, parsed.text.count { it == '\n' })
    }
}
