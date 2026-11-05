package com.jetbrains.godot.gdscript.utils

import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import gdscript.utils.stringContentRange
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["GDScript/Completion"])
class GdStringContentUtilTest {

    @ParameterizedTest(name = "{0}")
    @MethodSource("stringContentRanges")
    fun testStringContentRanges(text: String, expected: String) {
        assertEquals(expected, stringContentRange(text).substring(text))
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("closingDelimitersAfterBackslashes")
    fun testClosingDelimitersAfterBackslashes(text: String, expected: String) {
        assertEquals(expected, stringContentRange(text).substring(text), text)
    }

    companion object {
        @JvmStatic
        private fun stringContentRanges(): List<Arguments> = buildList {
            // Keep each literal and expected range independent, including empty and unclosed strings.
            for (prefix in listOf("", "&", "r")) {
                for (quote in listOf("\"", "'")) {
                    for (closed in listOf(false, true)) {
                        for (content in listOf("value", "")) {
                            val text = prefix + quote + content + if (closed) quote else ""
                            add(Arguments.of(text, content))
                        }
                    }
                }
            }
            for (quote in listOf("\"\"\"", "'''")) {
                for (closed in listOf(false, true)) {
                    val text = quote + "value" + if (closed) quote else ""
                    add(Arguments.of(text, "value"))
                }
            }
            add(Arguments.of("^'value'", "value"))
            add(Arguments.of("'value\\'", "value\\'"))
        }

        @JvmStatic
        private fun closingDelimitersAfterBackslashes(): List<Arguments> = buildList {
            for ([prefix, quote] in listOf("" to "\"", "&" to "\"", "&" to "'", "r" to "\"", "r" to "'",
                                         "" to "\"\"\"", "" to "'''")) {
                for (backslashCount in listOf(1, 2)) {
                    val content = "value" + "\\".repeat(backslashCount)
                    val text = prefix + quote + content + quote
                    add(Arguments.of(text, content))
                }
            }
            for (prefix in listOf("", "^")) {
                for (backslashCount in listOf(1, 2)) {
                    val content = "value" + "\\".repeat(backslashCount)
                    val text = prefix + "'" + content + "'"
                    val expected = content + if (backslashCount % 2 != 0) "'" else ""
                    add(Arguments.of(text, expected))
                }
            }
        }
    }
}
