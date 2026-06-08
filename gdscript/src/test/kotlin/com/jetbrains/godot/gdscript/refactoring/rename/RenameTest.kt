package com.jetbrains.godot.gdscript.refactoring.rename

import com.intellij.psi.PsiDocumentManager
import com.jetbrains.godot.gdscript.resolve.ResolveTestBase
import com.jetbrains.godot.getBaseTestDataPath
import kotlin.io.path.pathString

class RenameTest : ResolveTestBase() {

    override fun getTestDataPath(): String {
        return getBaseTestDataPath().resolve("testData/gdscript/refactoring").pathString
    }

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
        myFixture.configureByText("rename_var.gd", code)
        myFixture.renameElementAtCaret("renamed_A1Instance")

        // Commit and run highlighting to simulate IDE analysis (and warm caches)
        PsiDocumentManager.getInstance(project).commitAllDocuments()
        myFixture.doHighlighting() // forces resolve/indices/inspections

        // Verify the text was updated accordingly
        val updated = myFixture.file.text
        assertTrue(updated.contains("var renamed_A1Instance := A1.new()"))
        assertTrue(updated.contains("renamed_A1Instance.ppa1()"))

        // Dump resolves and assert there are no unresolved references
        val dump = dumpResolvesWithInlineMarkers(myFixture.file)
        assertGold(dump)
    }

    fun testRenameVariableShortName() {
        val code = """
        |    func _ready() -> void:
	    |        var test_long_name := 1
	    |        print(test_long<caret>_name)
        """.trimMargin()

        myFixture.configureByText("rename_var_short.gd", code)
        myFixture.renameElementAtCaret("test_name")

        PsiDocumentManager.getInstance(project).commitAllDocuments()
        myFixture.doHighlighting()

        val updated = myFixture.file.text
        assertTrue("expected 'var test_name := 1' but got:\n$updated", updated.contains("var test_name := 1"))
        assertTrue("expected 'print(test_name)' but got:\n$updated", updated.contains("print(test_name)"))
    }
}