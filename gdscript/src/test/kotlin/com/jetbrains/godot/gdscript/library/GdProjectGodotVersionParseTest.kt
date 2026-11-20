package com.jetbrains.godot.gdscript.library

import com.intellij.openapi.util.Version
import com.jetbrains.rider.godot.community.GdProjectGodotService
import com.jetbrains.rider.godot.community.GdProjectGodotService.ParseResult
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["GDScript/SDK"])
class GdProjectGodotVersionParseTest {

    @Test
    fun testVersionFromFeatures() {
        val content = """
            [application]
            config/name="Test"
            config/features=PackedStringArray("4.3", "Forward Plus")
        """.trimIndent()

        @Suppress("KotlinMisorderedAssertEqualsArguments")
        assertEquals(ParseResult.Parsed(Version(4, 3, 0)), GdProjectGodotService.parseVersion(content))
    }

    @Test
    fun testTwoDigitMinorVersion() {
        val content = "config/features=PackedStringArray(\"4.10\", \"Forward Plus\")"

        @Suppress("KotlinMisorderedAssertEqualsArguments")
        assertEquals(ParseResult.Parsed(Version(4, 10, 0)), GdProjectGodotService.parseVersion(content))
    }

    @Test
    fun testMissingFeaturesGiveUnknown() {
        val content = """
            [application]
            config/name="Test"
        """.trimIndent()

        assertEquals(ParseResult.Unknown, GdProjectGodotService.parseVersion(content))
    }

    @Test
    fun testGodot3GivesUnsupported() {
        val content = "config/features=PackedStringArray(\"3.5\")"

        assertEquals(ParseResult.Unsupported, GdProjectGodotService.parseVersion(content))
    }
}
