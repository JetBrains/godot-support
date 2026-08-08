package com.jetbrains.godot.gdscript.completion

import com.intellij.codeInsight.lookup.Lookup
import com.intellij.codeInsight.lookup.LookupElementPresentation
import com.intellij.testFramework.fixtures.CodeInsightTestFixture
import com.jetbrains.godot.gdscript.GdTestCaseWithSdk
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class GdCompletionTest : GdTestCaseWithSdk("completion") {

    @Test
    fun testCompleteClassMembers() = doLookupTest(
        dir = false
    )

    @Test
    fun testCompletionAfterConstructedInnerClass() = doLookupTest(
        dir = false,
        fileContents = """
            |class_name NestedClassErrors
            |
            |func in_the_outer_wrong():
            |    A1.new().<caret>
            |
            |class A1:
            |    func ppa1():
            |        pass
            |    class B1:
            |        class C1:
            |            class D1:
            |                func pp():
            |                    pass
        """.trimMargin(),
    )

    @Test
    fun testCompletionAfterClass() = doLookupTest(
        dir = false,
        fileContents = """
            |class_name NestedClassErrors
            |
            |func in_the_outer_wrong():
            |    A1.<caret>
            |
            |class A1:
            |    func ppa1():
            |        pass
            |    class B1:
            |        class C1:
        """.trimMargin(),
    )

    @Test
    fun testCompletionAfterClass2() = doLookupTest(
        dir = false,
        fileContents = """
            |class_name NestedClassErrors
            |
            |func in_the_outer_wrong():
            |    A1.B1.<caret>
            |
            |class A1:
            |    func ppa1():
            |        pass
            |    class B1:
            |        class C1:
        """.trimMargin(),
    )

    @Test
    fun testWithSdk01() = doLookupTest(
        dir = false,
        fileContents = """
            |func a():
            |	var previous_mode := Input.<caret> #anonymous enum in the static class context
        """.trimMargin(),
        lookupItemFilter = {
            it.displayText?.startsWith("MOUSE_") == true
        }
    )

    @Test
    fun testWithSdk02() = doLookupTest(
        dir = false,
        fileContents = """
            |func a():
            |	var previous_mode := Input.<caret> #anonymous enum in the static class context
        """.trimMargin(),
        lookupItemFilter = {
            it.displayText?.startsWith("action_") == true
        }
    )

    @Test
    fun testQuotedUniqueNameLookup() = doUniqueNamesTest("%\"<caret>\"") {
        val items = lookupElementStrings!!
        assertContainsElements(items, "%\"HealthBar\"", "%\"Score Label\"", "%\"Main-Menu\"")
        assertDoesntContain(items, "%\"Plain\"", "%\"Main\"", "\$Plain")
    }

    @Test
    fun testQuotedUniqueNameInsertsWithoutDuplicateQuote() = doUniqueNamesTest("%\"<caret>\"") {
        val lookup = lookup!!
        lookup.currentItem = lookup.items.single { it.lookupString == "%\"Score Label\"" }
        finishLookup(Lookup.NORMAL_SELECT_CHAR)

        assertEquals(script("%\"Score Label\""), editor.document.text)
    }

    /**
     * The unquoted `%<caret>` form stays unquoted for names Godot can parse as an identifier and
     * switches to `%"..."` for the ones it cannot.
     */
    @Test
    fun testUnquotedUniqueNameLookupQuotesOnlyWhenRequired() = doUniqueNamesTest("%<caret>") {
        val items = lookupElementStrings!!
        assertContainsElements(items, "%HealthBar", "%\"Score Label\"", "%\"Main-Menu\"")
        assertDoesntContain(items, "%Score Label", "%Main-Menu", "%Plain")
    }

    @Test
    fun testInstancedNodesAreOffered() = doUniqueNamesTest("%\"<caret>\"") {
        val items = lookupElementStrings!!
        assertContainsElements(items, "%\"Player\"")
        assertDoesntContain(items, "%\"PlayerRoot\"")

        val player = lookup!!.items.single { it.lookupString == "%\"Player\"" }
        assertEquals("CharacterBody2D", LookupElementPresentation.renderElement(player).typeText)
    }

    @Test
    fun testInstancedNodeChildrenAreOffered() = doUniqueNamesTest("\$<caret>") {
        assertContainsElements(lookupElementStrings!!, "\$Player", "\$Player/Sprite")
    }

    @Test
    fun testInstancedNodeUniqueChildrenAreNotOffered() = doUniqueNamesTest("%\"<caret>\"") {
        val items = lookupElementStrings!!
        assertDoesntContain(items, "%\"PlayerUnique\"")
    }

    @Test
    fun testTscnSelfRecursion() = doSelfTscnRecursionTest("\$<caret>") {
        assertContainsElements(lookupElementStrings!!, "\$Main/SelfRec", "\$Main")
    }

    @Test
    fun testTscnMutualRecursion() = doMutualTscnRecursionTest("\$<caret>") {
        // "\$MainRec1/Rec1/Rec2" is not there, because the recursion guard fires on it
        assertContainsElements(lookupElementStrings!!, "\$MainRec1/Rec1", "\$MainRec1")
    }

    private fun script(expression: String): String =
        "extends Node2D\n\nfunc _ready():\n\tvar node = $expression\n"

    private fun doUniqueNamesTest(expression: String, check: CodeInsightTestFixture.() -> Unit) =
        doConfiguredTest(
            dir = true,
            dirName = "uniqueNames",
            configureFileName = "unique_names.gd",
            fileContents = script(expression),
        ) {
            completeBasic()
            check()
        }

    private fun doSelfTscnRecursionTest(expression: String, check: CodeInsightTestFixture.() -> Unit) =
        doConfiguredTest(
            dir = true,
            dirName = "tscnSelfRecursion",
            configureFileName = "tscn_recursion.gd",
            fileContents = script(expression),
        ) {
            completeBasic()
            check()
        }

    private fun doMutualTscnRecursionTest(expression: String, check: CodeInsightTestFixture.() -> Unit) =
        doConfiguredTest(
            dir = true,
            dirName = "tscnMutualRecursion",
            configureFileName = "tscn_recursion.gd",
            fileContents = script(expression),
        ) {
            completeBasic()
            check()
        }

}
