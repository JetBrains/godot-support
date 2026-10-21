package com.jetbrains.godot.gdscript.completion

import com.intellij.codeInsight.lookup.Lookup
import com.intellij.codeInsight.lookup.LookupElementPresentation
import com.intellij.testFramework.fixtures.CodeInsightTestFixture
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdSdkFixture
import com.jetbrains.godot.gdscript.gdSdkFixture
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@TestApplication
class GdCompletionTest {
    private val gd: GdSdkFixture by gdSdkFixture("completion")

    @Test
    fun testCompleteClassMembers() = gd.doLookupTest(
        dir = false
    )

    @Test
    fun testCompletionAfterConstructedInnerClass() = gd.doLookupTest(
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
    fun testCompletionAfterClass() = gd.doLookupTest(
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
    fun testCompletionAfterClass2() = gd.doLookupTest(
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
    fun testWithSdk01() = gd.doLookupTest(
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
    fun testWithSdk02() = gd.doLookupTest(
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
    fun testInputActionsPrecedeResourcesInQualifiedCall() =
        doInputActionRankingTest("Input.is_action_pressed(\"<caret>\")")

    @Test
    fun testInputActionsPrecedeResourcesInUnqualifiedCall() =
        doInputActionRankingTest("is_action_pressed(\"<caret>\")")

    @Test
    fun testInputActionsPrecedeResourcesInInputMap() =
        doInputActionRankingTest("InputMap.has_action(\"<caret>\")")

    @Test
    fun testQuotedUniqueNameLookup() = doUniqueNamesTest("%\"<caret>\"") {
        val items = lookupElementStrings!!
        assertThat(items).contains("%\"HealthBar\"", "%\"Score Label\"", "%\"Main-Menu\"")
        assertThat(items).doesNotContain("%\"Plain\"", "%\"Main\"", "\$Plain")
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
        assertThat(items).contains("%HealthBar", "%\"Score Label\"", "%\"Main-Menu\"")
        assertThat(items).doesNotContain("%Score Label", "%Main-Menu", "%Plain")
    }

    @Test
    fun testInstancedNodesAreOffered() = doUniqueNamesTest("%\"<caret>\"") {
        val items = lookupElementStrings!!
        assertThat(items).contains("%\"Player\"")
        assertThat(items).doesNotContain("%\"PlayerRoot\"")

        val player = lookup!!.items.single { it.lookupString == "%\"Player\"" }
        assertEquals("CharacterBody2D", LookupElementPresentation.renderElement(player).typeText)
    }

    @Test
    fun testInstancedNodeChildrenAreOffered() = doUniqueNamesTest("\$<caret>") {
        assertThat(lookupElementStrings!!).contains("\$Player", "\$Player/Sprite")
    }

    @Test
    fun testInstancedNodeUniqueChildrenAreNotOffered() = doUniqueNamesTest("%\"<caret>\"") {
        val items = lookupElementStrings!!
        assertThat(items).doesNotContain("%\"PlayerUnique\"")
    }

    @Test
    fun testTscnSelfRecursion() = doSelfTscnRecursionTest("\$<caret>") {
        assertThat(lookupElementStrings!!).contains("\$Main/SelfRec", "\$Main")
    }

    @Test
    fun testTscnMutualRecursion() = doMutualTscnRecursionTest("\$<caret>") {
        // "\$MainRec1/Rec1/Rec2" is not there, because the recursion guard fires on it
        assertThat(lookupElementStrings!!).contains("\$MainRec1/Rec1", "\$MainRec1")
    }

    private fun script(expression: String): String =
        "extends Node2D\n\nfunc _ready():\n\tvar node = $expression\n"

    /** @param expression is expected to include the `<caret>` string */
    private fun doInputActionRankingTest(expression: String) =
        gd.doConfiguredTest(
            dir = true,
            dirName = "inputActions",
            configureFileName = "input_actions.gd",
            fileContents = "extends Node\n\nfunc _ready():\n\t$expression\n",
        ) {
            completeBasic()
            val items = lookup!!.items.map { it.lookupString }
            assertThat(items).contains("\"ui_accept\"", "\"move_left\"", "\"res://resource.gd\"")

            val actionIndices = items.indices.filter { items[it] == "\"ui_accept\"" || items[it] == "\"move_left\"" }
            val resourceIndices = items.indices.filter { items[it].trim('"').startsWith("res://") }
            assertTrue(actionIndices.max() < resourceIndices.min(), "Input actions must precede resources: $items")
        }

    private fun doUniqueNamesTest(expression: String, check: CodeInsightTestFixture.() -> Unit) =
        gd.doConfiguredTest(
            dir = true,
            dirName = "uniqueNames",
            configureFileName = "unique_names.gd",
            fileContents = script(expression),
        ) {
            completeBasic()
            check()
        }

    private fun doSelfTscnRecursionTest(expression: String, check: CodeInsightTestFixture.() -> Unit) =
        gd.doConfiguredTest(
            dir = true,
            dirName = "tscnSelfRecursion",
            configureFileName = "tscn_recursion.gd",
            fileContents = script(expression),
        ) {
            completeBasic()
            check()
        }

    private fun doMutualTscnRecursionTest(expression: String, check: CodeInsightTestFixture.() -> Unit) =
        gd.doConfiguredTest(
            dir = true,
            dirName = "tscnMutualRecursion",
            configureFileName = "tscn_recursion.gd",
            fileContents = script(expression),
        ) {
            completeBasic()
            check()
        }

}
