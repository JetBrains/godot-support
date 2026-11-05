package com.jetbrains.godot.gdscript.completion

import com.intellij.codeInsight.lookup.Lookup
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.CodeInsightTestFixture
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.DEFAULT_SDK_DIRECTORIES
import com.jetbrains.godot.gdscript.GdSdkFixture
import com.jetbrains.godot.gdscript.gdSdkFixture
import com.jetbrains.godot.gdscript.util.disableSingleItemAutoInsert
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import gdscript.psi.GdStringValRef
import gdscript.reference.GdResourceReference
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Named.named
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource

@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["GDScript/Completion"])
@TestApplication
class GdStringCompletionTest {
    private val gd: GdSdkFixture by gdSdkFixture(
        "gdscript/completion",
        DEFAULT_SDK_DIRECTORIES + listOf("../sdk/inputActionMethods", "../sdk/inputActionDefaults"),
    )

    @Test
    fun testOnlyInputActionsInQualifiedCall() =
        doActionTest("Input.is_action_pressed(\"<caret>\")") { assertActionsCompletionOnly() }

    @Test
    fun testInputActionsPrecedeResourcesInUnqualifiedCall() = doActionTest("is_action_pressed(\"<caret>\")") {
        val items = lookup!!.items.map { it.lookupString }
        assertThat(items).contains("ui_accept", "jump", "res://resource.gd", "enemies", "health")

        val actionIndices = items.indices.filter { items[it] == "ui_accept" || items[it] == "jump" }
        val resourceIndices = items.indices.filter { items[it].startsWith("res://") }
        assertTrue(actionIndices.max() < resourceIndices.min(), "Input actions must precede resources: $items")
    }

    // GdKeywordContributor offered keywords such as func and var inside string literals.
    @ParameterizedTest(name = "{0}")
    @ValueSource(strings = ["\"<caret>\"", "&\"<caret>\""])
    fun testNoKeywordsInStringLiterals(literal: String) = doActionTest("var s = $literal") {
        assertThat(lookupElementStrings.orEmpty().map { it.trim() }).doesNotContain("func", "var", "pass", "return")
    }

    @Test
    fun testPlainStringShowsProjectActionsWithoutDefaults() =
        doActionTest("var s = \"<caret>\"") { assertGeneralSuggestions() }

    @Test
    fun testDefaultsWithoutInputSection() = doActionTest("Input.is_action_pressed(\"<caret>\")", noInputs = true) {
        assertThat(lookupElementStrings!!).contains("ui_accept", "ui_cancel")
    }

    // ProjectSettings.xml adds TESTING_ONLY_NEWLY_UI_METHOD_TESTING to simulate a new SDK action.
    // It also has the platform entry ui_accept.macos, which is not an action.
    @Test
    fun testDefaultsComeFromProjectSettingsDocs() = doActionTest("Input.is_action_pressed(\"<caret>\")") {
        assertThat(lookupElementStrings!!).contains("TESTING_ONLY_NEWLY_UI_METHOD_TESTING")
        assertThat(lookupElementStrings!!).doesNotContain("ui_accept.macos")
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
    fun testDoubleQuotedStringNameActions() =
        doActionTest("Input.is_action_pressed(&\"<caret>\")") { assertActionsCompletionOnly() }

    // An unresolved call with a known input method name shows only actions inside a StringName literal.
    @Test
    fun testUnresolvedStringNameActions() =
        doActionTest("is_action_pressed(&\"<caret>\")") { assertActionsCompletionOnly() }

    @ParameterizedTest(name = "{2}: {0}")
    @MethodSource("contentOnlyInsertions")
    fun testContentOnlyInsertion(literal: String, expected: String, selectChar: Char) =
        doStringNameInsertionTest(literal, expected, selectChar)

    @Test
    fun testTabPreservesDoubleQuoteAfterBackslash() =
        doStringNameInsertionTest("\"ju<caret>nk\\\"", "\"jump\"", Lookup.REPLACE_SELECT_CHAR)

    @Test
    fun testUnclosedSingleQuoteCompletion() {
        disableSingleItemAutoInsert(gd.testRootDisposable)
        doActionTest("var s = 'ju<caret>") {
            lookup!!.currentItem = lookup!!.items.single { it.lookupString == "jump" }
            finishLookup(Lookup.REPLACE_SELECT_CHAR)
            val insertedContent = editor.document.text.substringAfter("var s = '").trimEnd('\n')
            assertEquals("jump", insertedContent)
        }
    }

    @Test
    fun testStringNameDoubleQuoteGroupsAndMetadata() = doActionTest("var s = &\"<caret>\"") {
        assertThat(lookupElementStrings!!).contains("enemies", "health")
        assertFalse(lookupElementStrings!!.any { it.startsWith("res://") })
    }

    @ParameterizedTest(name = "{0}")
    @ValueSource(strings = ["resource.gd", "input_actions.tscn", "image.png", "data.tres"])
    fun testAllResourceTypesAppearOnce(path: String) = doActionTest("var s = '<caret>'") {
        addFileToProject("image.png", "")
        addFileToProject("data.tres", "[gd_resource type=\"Resource\" format=3]\n")
        completeBasic()
        // The contributor offers all indexed resource files, including .png and .tres, not only .gd and .tscn.
        // Each file must appear once because this contributor is the only resource source.
        assertEquals(1, lookupElementStrings!!.count { it == "res://$path" }, path)
    }

    @Test
    fun testExtendsResourceCompletion() = gd.doConfiguredTest(
        dir = true, dirName = "inputActions", configureFileName = "extends_completion.gd",
        fileContents = "extends \"res://<caret>\"\n",
    ) {
        completeBasic()
        // Completion after extends must still offer resource paths.
        // A duplicate item from parent-class completion is known and outside this test's scope.
        assertThat(lookupElementStrings!!).contains("res://resource.gd")
    }

    @ParameterizedTest(name = "{0}{1}res://resource.gd{1}")
    @MethodSource("resourceQuoteForms")
    fun testResourcePathResolvesInEveryQuoteForm(prefix: String, quote: String) = gd.doConfiguredTest(
        dir = true, dirName = "inputActions", configureFileName = "resource_edit.gd",
        fileContents = "var s = $prefix${quote}res://resource.gd$quote\n",
    ) {
        val host = PsiTreeUtil.findChildOfType(file, GdStringValRef::class.java)!!
        val reference = host.references.filterIsInstance<GdResourceReference>().single()
        assertNotNull(reference.resolve())
        assertEquals("res://resource.gd", reference.rangeInElement.substring(host.text))
    }

    @ParameterizedTest(name = "{2}: {0}{1}res://resource.gd{1}")
    @MethodSource("resourceRenameCases")
    fun testResourceRenameKeepsQuotesAndPrefix(prefix: String, quote: String, bind: Boolean) = gd.doConfiguredTest(
        dir = true, dirName = "inputActions", configureFileName = "resource_edit.gd",
        fileContents = "var s = $prefix${quote}res://resource.gd$quote\n",
    ) {
        val host = PsiTreeUtil.findChildOfType(file, GdStringValRef::class.java)!!
        val reference = host.references.filterIsInstance<GdResourceReference>().single()
        val target = addFileToProject("renamed.gd", "extends Node\n")
        WriteCommandAction.runWriteCommandAction(project) {
            if (bind) reference.bindToElement(target) else reference.handleElementRename("renamed.gd")
        }
        assertEquals("var s = $prefix${quote}res://renamed.gd$quote\n", file.text)
    }

    private fun CodeInsightTestFixture.assertActionsCompletionOnly() {
        val items = lookup!!.items.map { it.lookupString }
        assertThat(items).contains("jump", "ui_accept", "ui_cancel")
        assertEquals(1, items.count { it == "ui_accept" })
        assertTrue(items.indexOf("jump") < items.indexOf("ui_cancel"))
        assertTrue(items.indexOf("ui_accept") < items.indexOf("ui_cancel"))
        assertThat(items).doesNotContain("res://resource.gd", "enemies", "health")
    }

    /** Checks project actions, resources, groups, and metadata without default-only actions. Resources precede actions. */
    private fun CodeInsightTestFixture.assertGeneralSuggestions() {
        val items = lookup!!.items.map { it.lookupString }
        assertThat(items).contains("jump", "res://resource.gd", "enemies", "health")
        assertThat(items).doesNotContain("ui_cancel")
        assertTrue(items.indexOf("res://resource.gd") < items.indexOf("jump"))
    }

    private fun doStringNameInsertionTest(
        literal: String,
        expected: String,
        selectChar: Char = Lookup.NORMAL_SELECT_CHAR,
        actionName: String = "jump",
    ) {
        disableSingleItemAutoInsert(gd.testRootDisposable)
        doActionTest("Input.is_action_pressed($literal)") {
            lookup!!.currentItem = lookup!!.items.single { it.lookupString == actionName }
            finishLookup(selectChar)
            assertEquals("extends Node\n\nfunc _ready():\n\tInput.is_action_pressed($expected)\n", editor.document.text)
        }
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

    companion object {
        private val quoteForms = listOf(
            "" to "\"", "" to "'", "&" to "\"", "&" to "'", "r" to "\"", "r" to "'",
            "" to "\"\"\"", "" to "'''"
        )

        @JvmStatic
        private fun contentOnlyInsertions(): List<Arguments> = buildList {
            // Enter inserts at the caret. Tab also replaces the remaining content.
            for ([prefix, quote] in listOf("", "&", "r").flatMap { p -> listOf(p to "'", p to "\"") }) {
                val expected = "$prefix${quote}jump$quote"
                add(Arguments.of("$prefix${quote}ju<caret>$quote", expected, named("Enter", Lookup.NORMAL_SELECT_CHAR)))
                add(Arguments.of("$prefix${quote}ju<caret>$quote", expected, named("Tab", Lookup.REPLACE_SELECT_CHAR)))
                add(Arguments.of("$prefix${quote}ju<caret>nk$quote", expected, named("Tab", Lookup.REPLACE_SELECT_CHAR)))
            }
            for (quote in listOf("\"\"\"", "'''")) {
                add(Arguments.of("${quote}ju<caret>nk$quote", "${quote}jump$quote", named("Tab", Lookup.REPLACE_SELECT_CHAR)))
            }
        }

        @JvmStatic
        private fun resourceQuoteForms(): List<Arguments> = quoteForms.map { [prefix, quote] -> Arguments.of(prefix, quote) }

        @JvmStatic
        private fun resourceRenameCases(): List<Arguments> = quoteForms.flatMap { [prefix, quote] ->
            listOf(
                Arguments.of(prefix, quote, named("rename", false)),
                Arguments.of(prefix, quote, named("bind", true)),
            )
        }
    }
}
