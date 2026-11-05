package com.jetbrains.godot.gdscript.completion

import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdSdkFixture
import com.jetbrains.godot.gdscript.gdSdkFixture
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

/**
 * Tests the built-in DEFAULT_ACTIONS fallback with an SDK that has no ProjectSettings.xml.
 * [GdSdkFixture.setUp] registers the SDK from the sdkDirectories of [gdSdkFixture].
 * [GdCompletionTest] adds ../sdk/inputActionDefaults, which contains ProjectSettings.xml.
 * This separate class uses the default SDK directories to test the fallback.
 */
@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["GDScript/Completion"])
@TestApplication
class GdInputActionFallbackCompletionTest {
    private val gd: GdSdkFixture by gdSdkFixture("gdscript/completion")

    @Test
    fun testDefaultsWithoutProjectSettingsDocs() = gd.doConfiguredTest(
        dir = true,
        dirName = "inputActions",
        configureFileName = "input_actions.gd",
        fileContents = "extends Node\n\nfunc _ready():\n\tInput.is_action_pressed(\"<caret>\")\n",
    ) {
        completeBasic()
        assertThat(lookupElementStrings!!).contains("ui_accept", "ui_cancel")
        assertThat(lookupElementStrings!!).doesNotContain("TESTING_ONLY_NEWLY_UI_METHOD_TESTING")
    }
}
