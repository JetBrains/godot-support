package com.jetbrains.godot.gdscript.inspection

import com.intellij.application.options.CodeStyle
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.jetbrains.godot.getBaseTestDataPath
import gdscript.inspection.GdWhitespaceLocalInspectionTool
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.io.path.pathString

@RunWith(JUnit4::class)
class GdWhitespaceLocalInspectionTest : BasePlatformTestCase() {

    override fun setUp() {
        super.setUp()
        myFixture.enableInspections(GdWhitespaceLocalInspectionTool::class.java)
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
        myFixture.configureByFile("${getTestName(false)}.gd")
        CodeStyle.getSettings(myFixture.file).getIndentOptionsByFile(myFixture.file).apply {
            USE_TAB_CHARACTER = true
            TAB_SIZE = 4
        }
        val fixes = myFixture.getAllQuickFixes()
        assertNotEmpty(fixes)
        // Applying the fix must not throw IndexOutOfBoundsException
        myFixture.launchAction(fixes.first())
    }

    /**
     * Regression test for the bug where the fix converted tab-indented lines to spaces
     * instead of removing the stray spaces, because it read indent options via the
     * generic `CodeStyle.getSettings(file).indentOptions` instead of the file-specific
     * `getIndentOptionsByFile(file)` that GdScript defaults to tabs.
     */
    @Test
    fun testFixIndentsKeepsTabsByDefault() {
        myFixture.configureByFile("${getTestName(false)}.gd")
        val fixes = myFixture.getAllQuickFixes()
        assertNotEmpty(fixes)
        myFixture.launchAction(fixes.first())
        myFixture.checkResultByFile("${getTestName(false)}_after.gd")
    }

    override fun getTestDataPath(): String {
        return getBaseTestDataPath().resolve("testData/gdscript/inspection").pathString
    }
}
