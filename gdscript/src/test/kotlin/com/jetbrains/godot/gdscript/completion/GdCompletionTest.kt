package com.jetbrains.godot.gdscript.completion

import com.intellij.codeInsight.lookup.Lookup
import com.intellij.codeInsight.lookup.LookupElementPresentation
import com.intellij.testFramework.fixtures.CodeInsightTestFixture
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.DEFAULT_SDK_DIRECTORIES
import com.jetbrains.godot.gdscript.GdSdkFixture
import com.jetbrains.godot.gdscript.gdSdkFixture
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["GDScript/Completion"])
@TestApplication
class GdCompletionTest {
    private val gd: GdSdkFixture by gdSdkFixture(
        "gdscript/completion",
        DEFAULT_SDK_DIRECTORIES + listOf("../sdk/inputActionMethods", "../sdk/inputActionDefaults"),
    )

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
    fun testOnlyInputActionsInQualifiedCall() =
        doActionTest("Input.is_action_pressed(\"<caret>\")") { assertActionsCompletionOnly() }

    @Test
    fun testInputActionsPrecedeResourcesInUnqualifiedCall() = doActionTest("is_action_pressed(\"<caret>\")") {
        val items = lookup!!.items.map { it.lookupString }
        assertThat(items).contains("\"ui_accept\"", "\"jump\"", "\"res://resource.gd\"", "\"enemies\"", "\"health\"")

        val actionIndices = items.indices.filter { items[it] == "\"ui_accept\"" || items[it] == "\"jump\"" }
        val resourceIndices = items.indices.filter { items[it].trim('"').startsWith("res://") }
        assertTrue(actionIndices.max() < resourceIndices.min(), "Input actions must precede resources: $items")
    }

    @Test
    fun testPlainStringShowsProjectActionsWithoutDefaults() =
        doActionTest("var s = \"<caret>\"") { assertGeneralSuggestions() }

    @Test
    fun testDefaultsWithoutInputSection() = doActionTest("Input.is_action_pressed(\"<caret>\")", noInputs = true) {
        assertThat(lookupElementStrings!!).contains("\"ui_accept\"", "\"ui_cancel\"")
    }

    // ProjectSettings.xml adds TESTING_ONLY_NEWLY_UI_METHOD_TESTING to simulate a new SDK action.
    @Test
    fun testDefaultsComeFromProjectSettingsDocs() = doActionTest("Input.is_action_pressed(\"<caret>\")") {
        assertThat(lookupElementStrings!!).contains("\"TESTING_ONLY_NEWLY_UI_METHOD_TESTING\"")
    }

    @Test
    fun testProjectSettingsXmlPlatformEntriesAreExcluded() = doActionTest("Input.is_action_pressed(\"<caret>\")") {
        assertThat(lookupElementStrings!!).doesNotContain("\"ui_accept.macos\"")
    }

    @ParameterizedTest(name = "{0}")
    @ValueSource(strings = ["Input.get_axis(\"<caret>\", \"\")", "Input.get_axis(\"\", \"<caret>\")"])
    fun testActionCompletionInMultipleArguments(expression: String) = doActionTest(expression) { assertActionsCompletionOnly() }

    @Test
    fun testGetVectorFloatArgumentKeepsGeneralSuggestions() =
        doActionTest("Input.get_vector(\"a\", \"b\", \"c\", \"d\", \"<caret>\")") { assertGeneralSuggestions() }

    // InputEventAction inherits is_action_pressed from InputEvent
    @Test
    fun testInputEventActionMethodUsesDeclaringClass() =
        doActionTest("var e: InputEventAction\n\te.is_action_pressed(\"<caret>\")") { assertActionsCompletionOnly() }

    // TouchScreenButton.set_action takes String, not the usual StringName.
    @Test
    fun testTouchScreenButtonSetActionStringParameter() =
        doActionTest("var b: TouchScreenButton\n\tb.set_action(\"<caret>\")") { assertActionsCompletionOnly() }

    @Test
    fun testMatchingMethodNameOnNonInputClassKeepsGeneralSuggestions() = doActionTest(
        "var obj := UserActions.new()\n\tobj.has_action(\"<caret>\")\n\nclass UserActions:\n\tfunc has_action(n: String):\n\t\tpass"
    ) { assertGeneralSuggestions() }

    @Test
    fun testNestedCallKeepsGeneralSuggestions() =
        doActionTest("Input.is_action_pressed(str(\"<caret>\"))") { assertGeneralSuggestions() }

    @Test
    fun testNestedArrayKeepsGeneralSuggestions() =
        doActionTest("Input.is_action_pressed([\"<caret>\"])") { assertGeneralSuggestions() }

    @Test
    fun testNestedDictionaryKeepsGeneralSuggestions() =
        doActionTest("Input.is_action_pressed(ACTION_MAP[\"<caret>\"])") { assertGeneralSuggestions() }

    @Test
    fun testDoubleQuotedStringNameActions() =
        doActionTest("Input.is_action_pressed(&\"<caret>\")") { assertActionsCompletionOnly(stringName = true) }

    @Test
    fun testSingleQuotedStringNameActions() =
        doActionTest("Input.is_action_pressed(&'<caret>')") { assertActionsCompletionOnly(stringName = true) }

    @Test
    fun testStringNamePrefixInsertion() = doStringNameInsertionTest("&\"ju<caret>\"", "&\"jump\"")

    @Test
    fun testStringNameSingleQuoteInsertion() = doStringNameInsertionTest("&'<caret>'", "&'jump'")

    @Test
    fun testStringNameTabReplacesRemainingContent() =
        doStringNameInsertionTest("&\"ju<caret>mp_old\"", "&\"jump\"", Lookup.REPLACE_SELECT_CHAR)

    // An unresolved call with a known input method name shows only actions inside a StringName literal.
    @Test
    fun testUnresolvedStringNameActions() =
        doActionTest("is_action_pressed(&\"<caret>\")") { assertActionsCompletionOnly(stringName = true) }

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

    private fun CodeInsightTestFixture.assertActionsCompletionOnly(stringName: Boolean = false) {
        val items = lookup!!.items.map { it.lookupString }
        fun action(name: String) = if (stringName) name else "\"$name\""
        assertThat(items).contains(action("jump"), action("ui_accept"), action("ui_cancel"))
        assertEquals(1, items.count { it == action("ui_accept") })
        assertTrue(items.indexOf(action("jump")) < items.indexOf(action("ui_cancel")))
        assertTrue(items.indexOf(action("ui_accept")) < items.indexOf(action("ui_cancel")))
        assertThat(items.map { it.trim('"') }).doesNotContain("res://resource.gd", "enemies", "health")
    }

    /** Checks project actions, resources, groups, and metadata without default-only actions. Resources precede actions. */
    private fun CodeInsightTestFixture.assertGeneralSuggestions() {
        val items = lookup!!.items.map { it.lookupString }
        assertThat(items).contains("\"jump\"", "\"res://resource.gd\"", "\"enemies\"", "\"health\"")
        assertThat(items).doesNotContain("\"ui_cancel\"")
        assertTrue(items.indexOf("\"res://resource.gd\"") < items.indexOf("\"jump\""))
    }

    private fun doStringNameInsertionTest(
        literal: String,
        expected: String,
        selectChar: Char = Lookup.NORMAL_SELECT_CHAR,
        actionName: String = "jump",
    ) =
        doActionTest("Input.is_action_pressed($literal)") {
            lookup!!.currentItem = lookup!!.items.single { it.lookupString == actionName }
            finishLookup(selectChar)
            assertEquals("extends Node\n\nfunc _ready():\n\tInput.is_action_pressed($expected)\n", editor.document.text)
        }

    /** @param noInputs removes the input section from project.godot. */
    private fun doActionTest(
        expression: String,
        noInputs: Boolean = false,
        check: CodeInsightTestFixture.() -> Unit,
    ) =
        gd.doConfiguredTest(
            dir = true,
            dirName = "inputActions",
            configureFileName = "action_completion.gd",
            fileContents = "extends Node\n\nfunc _ready():\n\t$expression\n",
        ) {
            if (noInputs) copyFileToProject("../project.godot", "project.godot")
            completeBasic()
            check()
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
