package com.jetbrains.godot.gdscript.inspection

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.jetbrains.godot.getBaseTestDataPath
import gdscript.inspection.GdTypeHintInspection
import gdscript.settings.GdProjectSettingsState
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.io.path.pathString

@RunWith(JUnit4::class)
class GdTypeHintInspectionTest : BasePlatformTestCase() {

    override fun setUp() {
        super.setUp()
        myFixture.enableInspections(GdTypeHintInspection::class.java)
    }

    @Test
    fun testTypeHintInferred() {
        myFixture.configureByText("test.gd", """
            <weak_warning descr="Field's return type can be specified as bool">var t = true</weak_warning>
        """.trimIndent())
        myFixture.testHighlighting(true, false, true)
    }

    @Test
    fun testTypeHintInferredQuickFixFull() {
        GdProjectSettingsState.getInstance(project).state.shortTyped = false
        myFixture.configureByText("test.gd", "<caret>var t = true")
        myFixture.doHighlighting()
        val actions = myFixture.availableIntentions
        val filtered = actions.filter { it.text.contains("type") || it.text.contains(":=)") }
        assertSize(2, filtered)
        // Auto-infer is alphabetically first
        assertEquals("Auto-infer type (:=)", filtered[1].text)
        assertEquals("Specify variable type [bool]", filtered[0].text)
        
        myFixture.launchAction(filtered[0])
        myFixture.checkResult("var t: bool = true")
    }

    @Test
    fun testTypeHintInferredQuickFixShort() {
        GdProjectSettingsState.getInstance(project).state.shortTyped = true
        myFixture.configureByText("test.gd", "<caret>var t = true")
        myFixture.doHighlighting()
        val actions = myFixture.availableIntentions
        val filtered = actions.filter { it.text.contains("type") || it.text.contains(":=)") }
        assertSize(2, filtered)
        // Auto-infer is alphabetically first
        assertEquals("Auto-infer type (:=)", filtered[1].text)
        assertEquals("Specify variable type [bool]", filtered[0].text)
        
        myFixture.launchAction(filtered[1])
        myFixture.checkResult("var t := true")
    }

    override fun getTestDataPath(): String {
        return getBaseTestDataPath().resolve("testData/gdscript/inspection").pathString
    }
}
