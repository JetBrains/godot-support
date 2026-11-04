package com.jetbrains.godot.gdscript.inspection

import com.intellij.application.options.CodeStyle
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdFixture
import com.jetbrains.godot.gdscript.gdLightFixture
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import gdscript.inspection.GdWhitespaceLocalInspectionTool
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["GDScript/Inspections"])
@TestApplication
class GdWhitespaceLocalInspectionTest {
    private val gd: GdFixture by gdLightFixture("gdscript/inspection")

    @BeforeEach
    fun enableWhitespaceInspection() {
        gd.fixture.enableInspections(GdWhitespaceLocalInspectionTool::class.java)
    }

    /**
     * Regression test for RIDER-139721: GdFixIndentsQuickFix throws IndexOutOfBoundsException
     * when applied to a file with space-indented lines and USE_TAB_CHARACTER=true.
     *
     * The bug: `range` is captured before `ConvertIndentsUtil.convertIndentsToTabs` runs.
     * That call replaces 4 spaces with 1 tab per line, shrinking the document.
     * Then `stripSpacesFromIndents` calls `document.getLineNumber(range.endOffset)` with
     * the now-stale offset that points past the end of the document, causing IOOBE.
     */
    @Test
    fun testFixIndentsDoesNotThrowIndexOutOfBounds() {
        gd.fixture.configureByFile("FixIndentsDoesNotThrowIndexOutOfBounds.gd")
        CodeStyle.getSettings(gd.fixture.file).getIndentOptionsByFile(gd.fixture.file).apply {
            USE_TAB_CHARACTER = true
            TAB_SIZE = 4
        }
        val fixes = gd.fixture.getAllQuickFixes()
        assertTrue(fixes.isNotEmpty())
        // Applying the fix must not throw IndexOutOfBoundsException
        gd.fixture.launchAction(fixes.first())
    }

    /**
     * Regression test for the bug where the fix converted tab-indented lines to spaces
     * instead of removing the stray spaces, because it read indent options via the
     * generic `CodeStyle.getSettings(file).indentOptions` instead of the file-specific
     * `getIndentOptionsByFile(file)` that GdScript defaults to tabs.
     */
    @Test
    fun testFixIndentsKeepsTabsByDefault() {
        gd.fixture.configureByFile("FixIndentsKeepsTabsByDefault.gd")
        val fixes = gd.fixture.getAllQuickFixes()
        assertTrue(fixes.isNotEmpty())
        gd.fixture.launchAction(fixes.first())
        gd.fixture.checkResultByFile("FixIndentsKeepsTabsByDefault_after.gd")
    }
}
