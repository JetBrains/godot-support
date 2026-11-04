package com.jetbrains.godot.gdscript.refactoring.rename

import com.intellij.openapi.application.EDT
import com.intellij.openapi.application.runReadActionBlocking
import com.intellij.polySymbols.testFramework.renameSymbolAtCaret
import com.intellij.psi.PsiDocumentManager
import com.intellij.testFramework.common.timeoutRunBlocking
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdResolveFixture
import com.jetbrains.godot.gdscript.gdResolveFixture
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["GDScript/Rename"])
@TestApplication
class RenameTest {
    private val gd: GdResolveFixture by gdResolveFixture("gdscript/refactoring")

    @Test
    fun testRenameVariable() {
        val code = """
            |func in_the_outer_wrong():
            |	var <caret>A1Instance := A1.new()
            |	renamed_A1Instance.ppa1() # will be updated after rename
            |
            |class A1:
            |	func ppa1():
            |		pass
        """.trimMargin()

        // Configure the file from text and perform rename at caret
        gd.fixture.configureByText("rename_var.gd", code)
        timeoutRunBlocking {
            withContext(Dispatchers.EDT) {
                gd.fixture.renameSymbolAtCaret("renamed_A1Instance")
                // Commit to simulate IDE analysis (and warm caches)
                PsiDocumentManager.getInstance(gd.project).commitAllDocuments()
            }
        }
        gd.fixture.doHighlighting() // forces resolve/indices/inspections

        // Verify the text was updated accordingly
        val updated = gd.fixture.file.text
        assertTrue(updated.contains("var renamed_A1Instance := A1.new()"))
        assertTrue(updated.contains("renamed_A1Instance.ppa1()"))

        // Dump resolves and assert there are no unresolved references
        val dump = runReadActionBlocking { gd.dumpResolvesWithInlineMarkers(gd.fixture.file) }
        gd.assertGold(dump)
    }

    @Test
    fun testRenameVariableShortName() {
        val code = """
        |    func _ready() -> void:
	    |        var test_long_name := 1
	    |        print(test_long<caret>_name)
        """.trimMargin()

        gd.fixture.configureByText("rename_var_short.gd", code)
        timeoutRunBlocking {
            withContext(Dispatchers.EDT) {
                gd.fixture.renameSymbolAtCaret("test_name")
                PsiDocumentManager.getInstance(gd.project).commitAllDocuments()
            }
        }
        gd.fixture.doHighlighting()

        val updated = gd.fixture.file.text
        assertTrue(updated.contains("var test_name := 1"), "expected 'var test_name := 1' but got:\n$updated")
        assertTrue(updated.contains("print(test_name)"), "expected 'print(test_name)' but got:\n$updated")
    }
}
