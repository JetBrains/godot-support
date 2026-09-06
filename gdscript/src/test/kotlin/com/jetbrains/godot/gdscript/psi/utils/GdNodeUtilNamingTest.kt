package com.jetbrains.godot.gdscript.psi.utils

import gdscript.psi.utils.GdNodeUtil
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.jupiter.api.assertAll
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class GdNodeUtilNamingTest {

    @Test
    fun testQuoteIfNeededQuotesWhatGodotCannotParseAsIdentifier() {
        assertAll(
            { assertQuoted("HealthBar", "HealthBar") },
            { assertQuoted("_private", "_private") },
            // Godot parses the unquoted `%Name` form as an identifier, so a leading digit needs quotes
            { assertQuoted("1abc", "\"1abc\"") },
            { assertQuoted("Score Label", "\"Score Label\"") },
            { assertQuoted("Main-Menu", "\"Main-Menu\"") },
            { assertQuoted("UI (Root)", "\"UI (Root)\"") },
        )
    }

    @Test
    fun testNodeNameToIdentifier() {
        assertAll(
            { assertIdentifier("HealthBar", "health_bar") },
            { assertIdentifier("Score Label", "score_label") },
            { assertIdentifier("Main-Menu", "main_menu") },
            // punctuation collapses instead of leaving a run of underscores behind
            { assertIdentifier("UI (Root)", "ui_root") },
            { assertIdentifier("HP+", "hp") },
            { assertIdentifier("(Root)", "root") },
            // camelToSnakeCase itself drops a leading separator, so the underscore does not survive
            { assertIdentifier("_Private", "private") },
            // an identifier cannot start with a digit
            { assertIdentifier("1abc", "_1_abc") },
            // GDScript allows unicode identifiers
            { assertIdentifier("Ünïcode", "ünïcode") },
            { assertIdentifier("c++c++", "c_c") },
            // nothing usable left
            { assertIdentifier("+++", "node") }
        )
    }

    private fun assertQuoted(name: String, expected: String) =
        assertEquals("quoteIfNeeded(\"$name\")", expected, GdNodeUtil.quoteIfNeeded(name))

    private fun assertIdentifier(name: String, expected: String) =
        assertEquals("nodeNameToIdentifier(\"$name\")", expected, GdNodeUtil.nodeNameToIdentifier(name))

}
